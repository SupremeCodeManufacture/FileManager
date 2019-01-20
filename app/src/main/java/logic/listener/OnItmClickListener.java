package logic.listener;

import java.io.File;
import java.util.List;

public interface OnItmClickListener {

    void onItemClicked(File file);

    void onCutomCatSelected(String patternRegex, String title);

    void onCutPasteListChanged(List<String> list);

}