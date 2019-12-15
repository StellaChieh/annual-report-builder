package cwb.cmt.surface.tools;

import java.util.ArrayList;

//import com.ibatis.sqlmap.client.SqlMapClient;

public abstract class ContextualComponent extends Component implements Contextual {

	private static ArrayList<Context> sAllContexts = new ArrayList<Context>();

    private Context mContext;
	
	public Context getContext() {
		return mContext;
	}
	
	protected void setContext(Context context) {
		mContext = context;
	}

	protected void init() {
		
	}

	protected void destroy() {
		
	}
	
	protected abstract boolean isContextInitialized();
	
	
	   
    protected static void addContext(Context context){
        if (!sAllContexts.contains(context)) {
            sAllContexts.add(context);
        }
    }
    
//    protected static void updateWrappedRawContext(SqlMapClient sqlMapClient) {
//        for (Context ctx : sAllContexts) {
//            if (ctx instanceof ContextWrapper)
//                ((ContextWrapper) ctx).updateBaseContext(sqlMapClient);
//        }
//    }
}
