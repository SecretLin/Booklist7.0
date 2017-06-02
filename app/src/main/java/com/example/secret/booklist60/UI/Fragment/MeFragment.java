package com.example.secret.booklist60.UI.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.Follow_Fans;
import com.example.secret.booklist60.DataBase.History;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.DataBase.Shoucang;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.UI.AboutusActivity;
import com.example.secret.booklist60.UI.FansActivity;
import com.example.secret.booklist60.UI.FeedBackActivity;
import com.example.secret.booklist60.UI.FollowActivity;
import com.example.secret.booklist60.UI.HistoryRecordActivity;
import com.example.secret.booklist60.UI.MyInteractionActivity;
import com.example.secret.booklist60.UI.MyPrivateConversationActivity;
import com.example.secret.booklist60.UI.SettingActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.example.secret.booklist60.R.id.btnAboutUs;

/**
 * Created by Secret on 2016/8/27.
 * 个人中心界面
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    ImageView ivCover1, ivCover2, ivCover3;
    CircleImageView circleImageView;//显示头像
    ImageButton btnMore, btnSetting, btnMyinter, btnMyprivateconversation, btnAboutus, btnFeedback;
    RelativeLayout rlMore;
    TextView tvHistory, tvFollower_count, tvFans_count, tvShoucang_count,tvHistory_null;
    public static TextView tvUsername;
    public static EditText etUsername;
    public static ImageButton btnModifyName;
    LinearLayout layout_Shoucang, layout_Guanzhu, layout_Fans;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyUser currentUser = BmobUser.getCurrentUser(getActivity(),MyUser.class);

        View view = inflater.inflate(R.layout.fragment_me, container, false);
        circleImageView = (CircleImageView) view.findViewById(R.id.imTouxiang_Me);
        circleImageView.setOnClickListener(this);

        ivCover1 = (ImageView) view.findViewById(R.id.ivCover1);
        ivCover2 = (ImageView) view.findViewById(R.id.ivCover2);
        ivCover3 = (ImageView) view.findViewById(R.id.ivCover3);



        tvUsername = (TextView) view.findViewById(R.id.tvUsername_Me);


        etUsername = (EditText) view.findViewById(R.id.etUsername_Me);

        etUsername.setImeOptions(EditorInfo.IME_ACTION_DONE);



        tvHistory = (TextView) view.findViewById(R.id.tvHistory);
        AssetManager mgr = getActivity().getAssets();//得到AssetManager
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/msyh.ttc");//根据路径得到Typeface
        tvHistory.setTypeface(tf);//设置字体

        tvHistory_null = (TextView) view.findViewById(R.id.tvHistory_null);
        tvHistory_null.setTypeface(tf);//设置字体

        tvShoucang_count = (TextView) view.findViewById(R.id.shoucang_count);

        tvFollower_count = (TextView) view.findViewById(R.id.tvFollower_count);

        tvFans_count = (TextView) view.findViewById(R.id.tvFans_count);








        btnMore = (ImageButton) view.findViewById(R.id.btnMore);
        btnMore.setOnClickListener(this);
        rlMore = (RelativeLayout) view.findViewById(R.id.rlMore);
        rlMore.setOnClickListener(this);
        btnSetting = (ImageButton) view.findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(this);
        layout_Shoucang = (LinearLayout) view.findViewById(R.id.layout_Shoucang);
        layout_Shoucang.setOnClickListener(this);
        btnMyinter = (ImageButton) view.findViewById(R.id.btnMyInter);
        btnMyinter.setOnClickListener(this);
        btnMyprivateconversation = (ImageButton) view.findViewById(R.id.btnMyPrivateConversation);
        btnMyprivateconversation.setOnClickListener(this);
        btnAboutus = (ImageButton) view.findViewById(btnAboutUs);
        btnAboutus.setOnClickListener(this);
        layout_Guanzhu = (LinearLayout) view.findViewById(R.id.layout_Guangzhu);
        layout_Guanzhu.setOnClickListener(this);
        layout_Fans = (LinearLayout) view.findViewById(R.id.layout_Fans);
        layout_Fans.setOnClickListener(this);
        btnModifyName = (ImageButton) view.findViewById(R.id.btnModifyName);

        btnFeedback = (ImageButton) view.findViewById(R.id.btnFeedback);
        btnFeedback.setOnClickListener(this);



        if (currentUser!=null){
            new Thread() {
                @Override
                public void run() {
                    queryFollower();
                    queryFans();
                    query_shoucang();

                }
            }.start();
            tvUsername.setText(currentUser.getUsername());
            etUsername.setText(currentUser.getUsername());
            etUsername.setSelection(currentUser.getUsername().length());
            if (currentUser.getHead() != null) {
                Glide.with(getActivity()).load(currentUser.getHead().getUrl()).into(circleImageView);
            }
            btnModifyName.setOnClickListener(this);
        }
        else {
            tvUsername.setText("游客");
            tvFans_count.setText("0");
            tvFollower_count.setText("0");
            tvShoucang_count.setText("0");
        }




        return view;
    }
    //获取关注的人数
    public void queryFollower() {
        MyUser currentUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        BmobQuery<Follow_Fans> query = new BmobQuery<>();
        boolean isCache = query.hasCachedResult(getContext(),Follow_Fans.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.addWhereEqualTo("Fans", new BmobPointer(currentUser));
        query.findObjects(getActivity(), new FindListener<Follow_Fans>() {
            @Override
            public void onSuccess(List<Follow_Fans> list) {
                tvFollower_count.setText(String.valueOf(list.size()));

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
   //获取粉丝的人数
    public void queryFans() {
        MyUser currentUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        BmobQuery<Follow_Fans> query = new BmobQuery<>();
        boolean isCache = query.hasCachedResult(getContext(),Follow_Fans.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.addWhereEqualTo("Follower", new BmobPointer(currentUser));
        query.findObjects(getActivity(), new FindListener<Follow_Fans>() {
            @Override
            public void onSuccess(List<Follow_Fans> list) {
                tvFans_count.setText(String.valueOf(list.size()));


            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    //获取收藏的数目
    private void query_shoucang() {
        MyUser currentUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        BmobQuery<Shoucang> query = new BmobQuery<>();
        boolean isCache = query.hasCachedResult(getContext(),Shoucang.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.addWhereEqualTo("myUser", new BmobPointer(currentUser));
        query.findObjects(getActivity(), new FindListener<Shoucang>() {
            @Override
            public void onSuccess(List<Shoucang> list) {
                //显示个人中心中收藏的数量
                tvShoucang_count.setText(String.valueOf(list.size()));


            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    public void onClick(final View view) {
        final MyUser currentUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        switch (view.getId()) {
            //点击头像可进行头像修改
            case R.id.imTouxiang_Me:
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                break;
            //点击历史记录整个格局，显示更多的历史记录
            case R.id.rlMore:
                startActivity(new Intent(getContext(), HistoryRecordActivity.class));
                break;
            //点击“更多”按钮，显示更多的历史记录
            case R.id.btnMore:
                startActivity(new Intent(getContext(), HistoryRecordActivity.class));
                break;
            //点击设置
            case R.id.btnSetting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            //点击上面的“收藏”的布局
            case R.id.layout_Shoucang:
//                Intent intent4 = new Intent(getContext(), ShoucangActivity.class);
//                intent4.putExtra("isCurrentUser",true);
//                startActivity(intent4);
                break;
            //点击上面“关注”的布局
            case R.id.layout_Guangzhu:
                Intent intent = new Intent(getContext(), FollowActivity.class);
                intent.putExtra("isCurrentUser",true);
                startActivity(intent);
                break;
            //点击上面“粉丝”的布局
            case R.id.layout_Fans:
                Intent intent3 = new Intent(getContext(), FansActivity.class);
                intent3.putExtra("isCurrentUser",true);
                startActivity(intent3);
                break;
            //点击“我的互动”的按钮
            case R.id.btnMyInter:
                startActivity(new Intent(getContext(), MyInteractionActivity.class));
                break;
            //点击“我的私信”的按钮
            case R.id.btnMyPrivateConversation:
                startActivity(new Intent(getContext(), MyPrivateConversationActivity.class));
                break;
            //点击用户名旁边的“笔”，可修改用户名
            case R.id.btnModifyName:
                tvUsername.setVisibility(View.GONE);
                etUsername.setVisibility(View.VISIBLE);
                btnModifyName.setVisibility(View.GONE);
                etUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(final TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            Log.i("完成:", "click");
                            currentUser.setUsername(etUsername.getText().toString());
                            currentUser.update(getActivity(), new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    toast("修改成功");
                                    tvUsername.setVisibility(View.VISIBLE);
                                    etUsername.setVisibility(View.GONE);
                                    tvUsername.setText(currentUser.getUsername());
                                    btnModifyName.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    toast("修改失败：" + s);
                                }
                            });
                            return true;

                        }
                        return false;
                    }
                });
                break;
            //点击“反映”按钮
            case R.id.btnFeedback:
                Intent intent2 = new Intent(getContext(), FeedBackActivity.class);
                intent2.putExtra("type","normal");
                startActivity(intent2);
                break;
            //点击“关于我们”的按钮
            case R.id.btnAboutUs:
                startActivity(new Intent(getContext(), AboutusActivity.class));
                break;


        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //加载头像
        MyUser currentUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        if (currentUser!=null){
            if (currentUser.getHead() != null) {
                Glide.with(getActivity()).load(currentUser.getHead().getUrl()).into(circleImageView);
            }
            else if (currentUser.getUrlHead()!=null){
                Glide.with(getActivity()).load(currentUser.getUrlHead()).into(circleImageView);
            }
            else {
                circleImageView.setImageResource(R.mipmap.head);
            }
            queryFollower();
            queryFans();
            query_shoucang();
            queryHistoryRecord();
        }

    }

    //查询历史记录
    public void queryHistoryRecord() {
        MyUser currentUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        BmobQuery<History> query = new BmobQuery<>();
        query.addWhereEqualTo("myUser", new BmobPointer(currentUser));
        query.order("-createdAt");
        query.include("booklist");

        query.findObjects(getActivity(), new FindListener<History>() {
            @Override
            public void onSuccess(List<History> list) {
                System.out.println("history:"+list.size());
                int num = list.size();
                switch (num) {
                    //没有历史记录
                    case 0:
                        break;
                    //只有一条历史记录
                    case 1:
//                        ivCover2.setVisibility(View.INVISIBLE);
//                        ivCover3.setVisibility(View.INVISIBLE);
                        Glide.with(getActivity()).load(list.get(0).getBook().getCover()).into(ivCover1);
                        ivCover1.setVisibility(View.VISIBLE);
                        tvHistory_null.setVisibility(View.GONE);
                        break;
                    //有两条历史记录
                    case 2:
//                        ivCover3.setVisibility(View.INVISIBLE);
                        Glide.with(getActivity()).load(list.get(0).getBook().getCover()).into(ivCover1);
                        Glide.with(getActivity()).load(list.get(1).getBook().getCover()).into(ivCover2);
                        ivCover1.setVisibility(View.VISIBLE);
                        ivCover2.setVisibility(View.VISIBLE);
                        tvHistory_null.setVisibility(View.GONE);
                        break;
                    //当超过三条，只会提取出最新的三条显示在“个人中心”界面
                    default:
                        Glide.with(getActivity()).load(list.get(0).getBook().getCover()).into(ivCover1);
                        Glide.with(getActivity()).load(list.get(1).getBook().getCover()).into(ivCover2);
                        Glide.with(getActivity()).load(list.get(2).getBook().getCover()).into(ivCover3);
                        ivCover1.setVisibility(View.VISIBLE);
                        ivCover2.setVisibility(View.VISIBLE);
                        ivCover3.setVisibility(View.VISIBLE);
                        btnMore.setVisibility(View.VISIBLE);
                        tvHistory_null.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onError(int i, String s) {
//                Log.d("Failed", s);
            }
        });
    }

    /*
    以下为选择图片与裁剪图片，并上传头像
     */
    private static String path = "/sdcard/myHead/";//sd路径
    String touxiangPath;
    private Bitmap head;//头像Bitmap

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final MyUser currentUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();

                    head = extras.getParcelable("data");

                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);//保存在SD卡中
                        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),null,"正在上传...");
                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                if (msg.what == 007) {

                                    Glide.with(getContext()).load(currentUser.getHead().getUrl()).into(circleImageView);
                                    Log.i("Receive:", "yes");
                                }
                            }
                        };

                        new Thread() {
                            @Override
                            public void run() {
                                System.out.println("run-----------------");
                                final BmobFile file1 = new BmobFile(new File(touxiangPath));
                                file1.uploadblock(getActivity(), new UploadFileListener() {
                                    @Override
                                    public void onSuccess() {
                                        System.out.println("--------------");
                                        currentUser.setHead(file1);
                                        currentUser.update(getActivity(), new UpdateListener() {
                                            @Override
                                            public void onSuccess() {
                                                progressDialog.dismiss();
                                                toast("更换头像成功");
                                                handler.sendEmptyMessage(007);
                                            }

                                            @Override
                                            public void onFailure(int i, String s) {
                                                toast(s);
                                            }
                                        });

                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }.start();
                    }
                }

                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    ;

    /**
     * 调用系统的裁剪
     *
     * @param uri
     *
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹

        String fileName = path + "head.jpg";//图片名字
        try {

            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            File file1 = new File(fileName);
            touxiangPath = file1.getAbsolutePath();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void toast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }


}