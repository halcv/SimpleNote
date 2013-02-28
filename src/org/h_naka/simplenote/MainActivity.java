
package org.h_naka.simplenote;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;
import android.widget.Button;
import android.widget.EditText;
import android.view.WindowManager.LayoutParams;

public class MainActivity extends Activity {

    private DataBaseHelper m_dbHelper;
    private NoteList m_noteList;
    private ListView m_itemListView;
    private Button m_saveButton;
    private EditText m_memoEdit;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        // ソフトキーボードを表示させない
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.main);

        initInstance();
        setListeners();
        m_dbHelper.dbOpen();
        m_noteList.loadNote();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_dbHelper.dbClose();
        m_dbHelper.close();
    }
    
    private void initInstance() {
        m_dbHelper = new DataBaseHelper(this);
        m_saveButton = (Button)findViewById(R.id.saveButton);
        m_memoEdit = (EditText)findViewById(R.id.memoEditText);
        m_itemListView = (ListView)findViewById(R.id.itemListView);
        m_noteList = new NoteList(this);
    }

    private void setListeners() {
        m_saveButton.setOnClickListener(m_noteList);
        m_itemListView.setOnItemClickListener(m_noteList);
    }
    
    public DataBaseHelper getDatabaseHelper() {
        return m_dbHelper;
    }

    public ListView getItemListView() {
        return m_itemListView;
    }

    public EditText getMemoEdit() {
        return m_memoEdit;
    }
}
