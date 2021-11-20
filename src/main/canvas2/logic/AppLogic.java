package canvas2.logic;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import canvas2.core.Updatable;
import canvas2.core.debug.TextTree;
import canvas2.time.FpsMeasurer;
import canvas2.time.TpfMeasurer;

/**
 * アプリケーションのロジックを管理する。<br>
 * 更新が必要なものを登録することで自動的に更新し続ける。<br>
 *
 */
public class AppLogic implements TextTree{

	private boolean isStop = true;
	private Thread mainLoop = this.createThread(this::run);

	private Set<Updatable> objSet;

	private TpfMeasurer tpf = this.createTpf(100);
	private FpsMeasurer fps = this.createFps();
	
	

	public AppLogic()
	{
		this.objSet = this.createSet();
	}
	
	protected Set<Updatable> createSet()
	{
		return new CopyOnWriteArraySet<>();
	}
	
	protected Thread createThread(Runnable r)
	{
		return new Thread(r);
	}
	
	protected TpfMeasurer createTpf(long tps)
	{
		return new TpfMeasurer(tps);
	}
	
	protected FpsMeasurer createFps()
	{
		return new FpsMeasurer();
	}
	
	
	

	/**
	 * 更新が必要なオブジェクトを追加します。
	 */
	public void add(Updatable obj)
	{
		this.objSet.add(obj);
	}

	/**
	 * 更新が必要なオブジェクトを削除します。
	 */
	public void remove(Updatable obj)
	{
		this.objSet.remove(obj);
	}

	/**
	 * 追加されたオブジェクトを全て削除します。
	 */
	public void clear()
	{
		this.objSet.clear();
	}
	
	/**
	 * Fpsを取得する。<br>
	 * 適切に扱うには、更新されている必要がある。
	 */
	public float getFps()
	{
		return this.fps.getFps();
	}
	
	/**
	 * Tpfを取得する。<br>
	 * 適切に扱うには、更新されている必要がある。
	 */
	public float getTpf()
	{
		return this.tpf.getTpf();
	}
	
	/**
	 * Tpfの目標Tickを設定する。
	 */
	public void setTargetTps(long tickPerSecond)
	{
		this.tpf.setTarget(tickPerSecond);
	}
	

	/**
	 * 更新を開始します。
	 */
	public void start()
	{
		this.isStop = false;
		this.mainLoop.start();
	}

	/**
	 * 更新を終了します。
	 */
	public void stop()
	{
		this.isStop = true;
	}

	/**
	 * 現在、追加したオブジェクトの更新を行っていないか。<br>
	 */
	public boolean isStop()
	{
		return this.isStop;
	}

	/**
	 * 更新を行う。
	 */
	protected void run()
	{
		while(!this.isStop())
		{
			this.updateObj(this.tpf.getTpf());
			this.tpf.update();
			this.fps.update();
		}
	}

	protected void updateObj(float tpf)
	{
		for(Updatable obj : this.objSet)
		{
			obj.updateAndThrow(tpf);
		}
	}

	@Override
	public StringBuilder createTreeText(StringBuilder sb, int nest)
	{
		String enter = System.lineSeparator();
		String tab1 = "\t".repeat(nest);
		String tab2 = tab1 + "\t";
		
		String title = this.getClass().getSimpleName();
		sb.append(tab1).append(title).append(enter);

		
		for(Updatable obj : this.objSet)
		{
			sb.append(tab2).append(obj).append(enter);
		}

		sb.append(enter);


		return sb;
	}

}
