package  com.visionarymindszm.examsresults.screens;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;
import com.visionarymindszm.examsresults.R;
import com.visionarymindszm.examsresults.utils.Utils;

import java.io.BufferedInputStream;
import java.io.File;


public class ViewPaperActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {
    PDFView pdfView;
    Uri uri;
    Uri localPath;
    Integer pageNumber = 0;
    String pdfName;
    String TAG = "ViewPaperActivityError";
    String url_pdf;
    String name, year;
    ConstraintLayout view_paper_layout;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_paper);
        Intent intent = getIntent();
        url_pdf = intent.getStringExtra(Utils.PDF_URL);
        name = intent.getStringExtra(Utils.PDF_NAME);
        year = intent.getStringExtra(Utils.PDF_YEAR);
        TextView paperName = findViewById(R.id.paperName);
        view_paper_layout = findViewById(R.id.view_paper_layout);
        uri  = Uri.parse(Utils.ROOT_URL+url_pdf);
        pdfView = findViewById(R.id.pdfView);

        downloadFile(Utils.ROOT_URL+url_pdf);
        String reading="You're reading "+name+"-"+year+".pdf";
        paperName.setText(reading);
        //download(uri);

        Log.d(TAG, uri.toString());
        Log.d(TAG, localPath.toString());
        // load the download file on to the screen
//        pdfView.fromUri(Uri.parse("file://"+localPath.toString())).load();
        displayFromUri(Uri.parse("file://"+localPath.toString()));

    }
    private void displayFromUri(Uri uri) {

        pdfView.fromUri(uri)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .load();
    }

    /**
     *
     * @param url: Link to the file destination
     */
    public void downloadFile(String url) {
        try {
            // check if previously the file was downloaded, if true
            File paper = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Exams/" + name + "-" + year + ".pdf");
            if (paper.exists()) {
                Log.d(TAG, "File present");
                localPath = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Exams/" + name + "-" + year + ".pdf");
                Utils.showSnackBar(view_paper_layout, "Loaded from files", -1);
            } else {

                DownloadManager downloadManager = (DownloadManager) getApplication().getSystemService(Context.DOWNLOAD_SERVICE);

                Uri downloadUri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(
                        downloadUri);

                request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI
                                | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false).setTitle("Downloading Paper")
                        .setDescription("Something useful.")
                        // save it to the downloads folder in a custom fold called exams
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Exams/" + name + "-" + year + ".pdf");
                Log.d(TAG, "" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Exams/" + name + "-" + year + ".pdf");
                localPath = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Exams/" + name + "-" + year + ".pdf");
                downloadManager.enqueue(request);

            }

        }catch (Exception e){
            Log.d(TAG, "Error ", e);
        }
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.d(TAG, "modDate = " + meta.getModDate());

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", name+"-"+year+".pdf", page + 1, pageCount));
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.d(TAG, "Cannot load page " + page);

    }
}
