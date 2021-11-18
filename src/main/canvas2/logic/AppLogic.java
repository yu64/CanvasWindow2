package canvas2.logic;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import canvas2.core.Updatable;
import canvas2.core.debug.TextTree;
import canvas2.time.TpfMeasurer;

/**
 * アプリケーションのロジックを管理する。<br>
 * 更新が必要なものを登録することで自動的に更新し続ける。<br>
 * tickを管理している。
 *
 */
public class AppLogic implements TextTree{

	private boolean isStop = true;
	private Thread mainLoop = new Thread(this::run);

	private Set<Updatable> objSet;

	public AppLogic()
	{
		this(new CopyOnWriteArraySet<>());
	}

	public AppLogic(Set<Updatable> objSet)
	{
		this.objSet = objSet;
	}


	public void add(Updatable obj)
	{
		this.objSet.add(obj);
	}

	public void remove(Updatable obj)
	{
		this.objSet.remove(obj);
	}

	public void clear()
	{
		this.objSet.clear();
	}

	public void start()
	{
		this.isStop = false;
		this.mainLoop.start();
	}

	public void stop()
	{
		this.isStop = true;
	}

	public boolean isStop()
	{
		return this.isStop;
	}

	private void run()
	{
		TpfMeasurer frame = new TpfMeasurer(100.0F);
		while(!this.isStop())
		{
			this.updateObj(frame.getTpf());
			frame.update();

		}
	}

	private void updateObj(float tpf)
	{
		for(Updatable obj : this.objSet)
		{
			try
			{
				obj.update(tpf);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public StringBuilder createTreeText(StringBuilder sb, int nest)
	{
		String enter = System.lineSeparator();
		String tab1 = "\t".repeat(nest);
		String title = this.getClass().getSimpleName();

		sb.append(tab1).append(title).append(enter);

		String tab2 = tab1 + "\t";

		for(Updatable obj : this.objSet)
		{
			sb.append(tab2).append(obj).append(enter);
		}

		sb.append(enter);


		return sb;
	}

}
