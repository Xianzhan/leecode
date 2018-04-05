package xianzhan.core.loader.hotload;

import xianzhan.core.loader.hotload.manager.IManager;

/**
 * 封装加载类的信息
 */
public class HotLoaderInfo {

    /**
     * 自定义的类加载
     */
    private HotClassLoader hotClassLoader;
    /**
     * 记录要加载的类的时间戳 --> 加载时间
     */
    private long loadTime;

    private IManager manager;

    public HotLoaderInfo(HotClassLoader hotClassLoader, long loadTime) {
        this.hotClassLoader = hotClassLoader;
        this.loadTime = loadTime;
    }

    public HotClassLoader getHotClassLoader() {
        return hotClassLoader;
    }

    public void setHotClassLoader(HotClassLoader hotClassLoader) {
        this.hotClassLoader = hotClassLoader;
    }

    public long getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(long loadTime) {
        this.loadTime = loadTime;
    }

    public IManager getManager() {
        return manager;
    }

    public void setManager(IManager manager) {
        this.manager = manager;
    }
}
