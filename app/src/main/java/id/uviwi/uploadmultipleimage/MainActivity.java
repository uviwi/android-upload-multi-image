package id.uviwi.uploadmultipleimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRvGambar;
    private Button mBtnOpenCamera, mBtnUpload;
    ArrayList<String> itemList;
    private ImageAdapter adapter;
    private static final String UPLOAD_URL = "http://10.0.2.2/_2021/learn/native/uploadmultipleimage/upload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemList = new ArrayList<>();
        adapter = new ImageAdapter(this, itemList);
        mRvGambar = findViewById(R.id.rv_gambar);
        mRvGambar.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvGambar.setLayoutManager(manager);
        mBtnOpenCamera = findViewById(R.id.btn_open_camera);
        mBtnUpload = findViewById(R.id.btn_upload);
        mBtnOpenCamera.setOnClickListener(this);
        mBtnUpload.setOnClickListener(this);
    }

    private void openCamera() {
        Pix.start(this, Options.init().setRequestCode(100).setCount(5));
    }

    private void uploadImage() {
        // Buat Variabel untuk menampung list file
        ArrayList<File> fileList = new ArrayList<>();
        // lakukan perulangan dari itemList untuk mengambil path file
        for (int i = 0; i < itemList.size(); i++) {
            // conversi path file yang di dapat dari itemList menjadi file
            File file = new File(itemList.get(i));
            // tambahkan file kedalam variabel fileList
            fileList.add(file);
        }
        HashMap<String, ArrayList<File>> fileMap = new HashMap<>();
        fileMap.put("file[]", fileList);
        AndroidNetworking.upload(UPLOAD_URL)
                .addMultipartFileList(fileMap)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Main", response.toString());
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {
                                // hilangkan semua file di itemList
                                itemList.clear();
                                //perbarui adapter
                                refresh();
                                // tampilkan button open camera dan hidden button upload
                                if (mBtnUpload.getVisibility() == View.VISIBLE) {
                                    mBtnUpload.setVisibility(View.GONE);
                                    mBtnOpenCamera.setVisibility(View.VISIBLE);
                                }
                            }
                            Toast.makeText(MainActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e("val", "requestCode ->  " + requestCode+"  resultCode "+resultCode);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                // tampilkan button upload dan hidden button open camera
                if (mBtnOpenCamera.getVisibility() == View.VISIBLE) {
                    mBtnOpenCamera.setVisibility(View.GONE);
                    mBtnUpload.setVisibility(View.VISIBLE);
                }
                itemList.addAll(data.getStringArrayListExtra(Pix.IMAGE_RESULTS));
                refresh();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnOpenCamera) {
            openCamera();
        }
        if (view == mBtnUpload) {
            uploadImage();
        }
    }
}