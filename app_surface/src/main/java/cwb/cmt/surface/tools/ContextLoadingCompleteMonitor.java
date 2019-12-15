package cwb.cmt.surface.tools;

import java.util.ArrayList;
import java.util.List;

public class ContextLoadingCompleteMonitor implements Context, ContextLoadingMonitor {

	List<OnContextLoadingCompleteListener> mListeners;
	
	public void addOnContextLoadingCompleteListener(OnContextLoadingCompleteListener listener) {
		if (mListeners == null)
			mListeners = new ArrayList<OnContextLoadingCompleteListener>();
		mListeners.add(listener);
	}
	
	public boolean removeOnContextLoadingCompleteListener(OnContextLoadingCompleteListener listener) {
		if (mListeners != null) {
			return mListeners.remove(listener);
		}
		return false;
	}
	
	public void invokeContextLoadingComplete() {
		if(mListeners != null) {
			for (int i=0; i<mListeners.size(); i++) {
				mListeners.get(i).onContextLoadingComplete();
			}
		}
	}
}
