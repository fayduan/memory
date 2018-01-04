package cn.duanyufei.memory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cn.duanyufei.app.AppDefine;
import cn.duanyufei.db.DBDao;
import cn.duanyufei.util.NetUtil;
import cn.duanyufei.util.StorageUtil;
import cn.duanyufei.util.ToastUtil;

public class BackupActivity extends AppCompatActivity implements View.OnClickListener {

    final static String TAG = "BackupActivity";

    Button btnBackup;
    Button btnRecover;
    EditText etName;
    EditText etCheck;
    View progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackupActivity.this.finish();
            }
        });

        btnBackup = findViewById(R.id.btn_backup);
        btnBackup.setOnClickListener(this);
        btnRecover = findViewById(R.id.btn_recover);
        btnRecover.setOnClickListener(this);
        etName = findViewById(R.id.et_name);
        etCheck = findViewById(R.id.et_check);
        progressView = findViewById(R.id.progress);

        if (StorageUtil.getName() != null) {
            etName.setText(StorageUtil.getName());
            if (StorageUtil.getCheck() != null) {
                etCheck.setText(StorageUtil.getCheck());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backup:
                setWait();
                String encode = encodeFile(this.getDatabasePath(DBDao.dbName));
                NetUtil.getInstance().post(AppDefine.BACKUP_URL,
                        String.format(AppDefine.DATA_FORMAT, etName.getText().toString(), etCheck.getText().toString(), encode),
                        new NetUtil.MOkCallBack() {
                            @Override
                            public void onSuccess(String rep) {
                                switch (rep) {
                                    case "0":
                                        ToastUtil.show(R.string.backup_0);
                                        StorageUtil.setName(etName.getText().toString());
                                        StorageUtil.setCheck(etCheck.getText().toString());
                                        break;
                                    case "1":
                                        ToastUtil.show(R.string.backup_1);
                                        break;
                                    case "2":
                                        ToastUtil.show(R.string.backup_2);
                                        break;
                                    case "3":
                                        ToastUtil.show(R.string.backup_3);
                                        break;
                                }
                                setOk();
                            }

                            @Override
                            public void onError() {
                                ToastUtil.show(R.string.net_error);
                                setOk();
                            }
                        });
                break;
            case R.id.btn_recover:
                setWait();
                NetUtil.getInstance().post(AppDefine.RECOVER_URL,
                        String.format(AppDefine.DATA_FORMAT, etName.getText().toString(), etCheck.getText().toString(), ""),
                        new NetUtil.MOkCallBack() {
                            @Override
                            public void onSuccess(String rep) {
                                if (rep.equals("nodata")) {
                                    ToastUtil.show(R.string.no_data);
                                } else {
                                    decodeFile(rep, BackupActivity.this.getDatabasePath(DBDao.dbName).getAbsolutePath());
                                    ToastUtil.show(R.string.recover_0);
                                }
                                setOk();
                            }

                            @Override
                            public void onError() {
                                ToastUtil.show(R.string.net_error);
                                setOk();
                            }
                        });
                break;
        }
    }

    private String encodeFile(File file) {
        String encodeStr = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodeStr = Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    private void decodeFile(String str, String filePath) {
        byte[] data = Base64.decode(str, Base64.NO_WRAP);
        try {
            OutputStream stream = new FileOutputStream(filePath);
            stream.write(data);
        } catch (Exception ignored) {
        }
    }

    private void setWait() {
        btnBackup.setClickable(false);
        btnRecover.setClickable(false);
        progressView.setVisibility(View.VISIBLE);
    }

    private void setOk() {
        btnBackup.setClickable(true);
        btnRecover.setClickable(true);
        progressView.setVisibility(View.INVISIBLE);
    }
}
