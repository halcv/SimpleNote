package org.h_naka.simplenote;

import java.util.Date;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import android.view.View;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.database.Cursor;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class NoteList
    implements OnClickListener,OnItemClickListener {

    private MainActivity m_activity;
    private ArrayAdapter<String> m_adapter;
    private ArrayList<String> m_arrayList;
    private int m_position;
    
    public NoteList(MainActivity activity) {
        m_activity = activity;
        m_arrayList = new ArrayList<String>();
        m_adapter = new ArrayAdapter<String>(m_activity,R.layout.row,m_arrayList);
        m_activity.getItemListView().setAdapter(m_adapter);
    }

    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.saveButton:
            onSaveButtonClicked();
            break;
        }
    }

    public void onItemClick(AdapterView<?> parent,View v,int pos,long id) {
        switch(v.getId()) {
        case R.id.itemTextView:
            m_position = pos;
            deleteNoteProcess();
            break;
        }
    }
    
    public void loadNote() {
        Cursor c = m_activity.getDatabaseHelper().getAllNotes();
        if (c.moveToFirst()) {
            do {
                StringBuilder builder = new StringBuilder();
                builder.append(c.getString(c.getColumnIndex(DBAdapter.COL_LASTUPDATE)));
                builder.append("\n");
                builder.append(c.getString(c.getColumnIndex(DBAdapter.COL_NOTE)));
                //m_adapter.add(builder.toString());
                m_adapter.insert(builder.toString(),0);
            } while(c.moveToNext());
        }
        c.close();
    }

    private void onSaveButtonClicked() {
        String str = m_activity.getMemoEdit().getText().toString();
        if (str.equals("")) {
            Toast.makeText(m_activity,m_activity.getResources().getString(R.string.no_note),Toast.LENGTH_LONG).show();
            return;
        }
        SimpleDateFormat sdf = (SimpleDateFormat)DateFormat.getDateTimeInstance();
        sdf.applyPattern("yyyy/MM/dd 'at' HH:mm:ss");
        String date = sdf.format(new Date());
        m_activity.getMemoEdit().setText("");
        String message = "";
        if(m_activity.getDatabaseHelper().saveNote(str,date)) {
            //m_adapter.add(date + "\n" + str);
            m_adapter.insert(date + "\n" + str,0);
            message = m_activity.getResources().getString(R.string.save_success);
        } else {
            message = m_activity.getResources().getString(R.string.save_error);
        }
        Toast toast = Toast.makeText(m_activity,message,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    private void deleteNoteProcess() {
        String note = (String)(m_adapter.getItem(getPostion()));
        String date = note.substring(0,22);
        new AlertDialog.Builder(m_activity)
            //.setIcon(R.drawable.icon)
            .setTitle(date + m_activity.getResources().getString(R.string.delete_note))
            .setPositiveButton(
                m_activity.getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        String note = (String)(m_adapter.getItem(getPostion()));
                        String date = note.substring(0,22);
                        String message = "";
                        if (m_activity.getDatabaseHelper().deleteNote(date)) {
                            m_adapter.remove(note);
                            message = m_activity.getResources().getString(R.string.delete_success);
                        } else {
                            message = m_activity.getResources().getString(R.string.delete_error);
                        }
                        Toast toast = Toast.makeText(m_activity,message,Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }).setNegativeButton(
                    m_activity.getResources().getString(R.string.cancel),
                    null
                    ).show();
    }

    private int getPostion() {
        return m_position;
    }
}
