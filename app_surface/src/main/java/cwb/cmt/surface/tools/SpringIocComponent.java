package cwb.cmt.surface.tools;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cwb.cmt.surface.tools.Component;
import cwb.cmt.surface.tools.Context;
import cwb.cmt.surface.tools.ContextLoadingCompleteMonitor;
import cwb.cmt.surface.tools.OnContextLoadingCompleteListener;

public class SpringIocComponent extends  cwb.cmt.surface.tools.IocComponent {
	
	private static org.springframework.context.ApplicationContext sSpringApplicationContext;

	private SpringIocContext mContext;
	
	private boolean contextInitialized;

	private static final ContextLoadingCompleteMonitor mLoadingListeningContext = new ContextLoadingCompleteMonitor();
	
	protected static void addOnContextLoadingCompleteListener(OnContextLoadingCompleteListener listener) {
		mLoadingListeningContext.addOnContextLoadingCompleteListener(listener);
	}
	
	protected static boolean removeOnApplicationContextLoadingCompleteListener(OnContextLoadingCompleteListener listener) {
		return mLoadingListeningContext.removeOnContextLoadingCompleteListener(listener);
	}
	
	protected static void invokeApplicationContextLoadingComplete() {
		mLoadingListeningContext.invokeContextLoadingComplete();
	}
	
	protected static void initApplicationContext(String xmlConfig) {
	    if (sSpringApplicationContext == null) {
        	try {
        		sSpringApplicationContext = new ClassPathXmlApplicationContext(xmlConfig);
        	}
        	catch(Exception e) {
        		e.printStackTrace();
        	}
        	finally {
        		if (sSpringApplicationContext != null)
        			invokeApplicationContextLoadingComplete();
        	}
	    }
	    else {
	        invokeApplicationContextLoadingComplete();
	    }
	}
	
	public Context getContext() {
		return mContext;
	}
	
	protected boolean isContextInitialized() {
		return sSpringApplicationContext != null;
	}
	
	public SpringIocComponent(/*TODO: String xmlConfig*/) {
		addOnContextLoadingCompleteListener(new OnContextLoadingCompleteListener() {
			public void onContextLoadingComplete() {
				setContext(mContext =  new SpringIocContext(sSpringApplicationContext));
                if (!contextInitialized) {
                    init();
                    if (sSpringApplicationContext != null) {
                        contextInitialized = true;
                    }
                }
                else {
                    reload();
                }
			}
		});
	}

	protected void init() {
		
	}

	protected void destroy() {
		
	}
	
    protected void reload() {
         init();
    }
	
	public boolean componentExists(String name) {
		return mContext.componentExists(name);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getComponent(String id) {
		return (T) mContext.getComponent(id);
	}

	@SuppressWarnings("unchecked")
	public <T> T getComponent(String id, Object... args) {
		return (T) mContext.getComponent(id, args);
	}
}
