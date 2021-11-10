package canvas2;

import canvas2.debug.TextTree;
import canvas2.event.EventManager;
import canvas2.event.awt.AwtDispatcher;
import canvas2.event.awt.AwtRelay;
import canvas2.logic.AppLogic;
import canvas2.view.AppWindow;
import canvas2.view.scene.Node;

/**
 * 主に必要な要素を集めたクラス。<br>
 * ルートノード、ウィンドウ、ロジック、イベントを含む。<br>
 * ここから使い始めることを推奨する。<br>
 *
 */
public class App implements AutoCloseable, TextTree{

	private Node root;
	private AppWindow window;
	private AppLogic logic;
	private EventManager event;

	public App()
	{
		this(
				new AppWindow(new Node("root")),
				new AppLogic(),
				new EventManager()
				);
	}

	public App(AppWindow window, AppLogic logic, EventManager event)
	{
		this.root = window.getRoot();
		this.window = window;
		this.logic = logic;
		this.event = event;
		this.setup();
	}

	public void init()
	{
		//初期化
		this.stop();
		this.root.clearChild();
		this.logic.clear();
		this.event.clearListener();
		this.event.clearDispatcher();
		this.event.clearRelay();
	}

	public void setup()
	{
		this.init();

		//描画更新のために、登録
		this.logic.add(this.window);

		//イベント実行のために、登録。
		this.logic.add(this.event);

		//ウィンドウのイベントを扱うディスパッチャーを登録。
		this.event.addDispatcher(new AwtDispatcher());

		//ウィンドウからイベントを取得するために、登録。
		this.event.addRelay(new AwtRelay(this.window));
	}

	/**
	 * アプリケーションの動作を開始する。
	 */
	public void start()
	{
		this.logic.start();
		this.window.setVisible(true);
	}

	/**
	 * アプリケーションの動作を停止する。
	 */
	public void stop()
	{
		this.logic.stop();
		this.window.setVisible(false);
	}

	/**
	 * アプリケーションを閉じる。
	 */
	@Override
	public void close()
	{
		this.stop();
		this.window.dispose();
	}

	/**
	 * {@link AppWindow}のルートノード({@link Node})を取得。
	 */
	public Node getRootNode()
	{
		return this.root;
	}

	/**
	 * {@link AppWindow}を取得。
	 */
	public AppWindow getWindow()
	{
		return this.window;
	}

	/**
	 * {@link AppLogic}を取得。
	 */
	public AppLogic getLogic()
	{
		return this.logic;
	}

	/**
	 * {@link EventManager}を取得。
	 */
	public EventManager getEventManager()
	{
		return this.event;
	}

	@Override
	public StringBuilder createTreeText(StringBuilder sb, int nest)
	{
		String enter = System.lineSeparator();
		String tab1 = "\t".repeat(nest);
		String title = this.getClass().getSimpleName();

		sb.append(tab1);
		sb.append(title);
		sb.append(enter);

		this.event.createTreeText(sb, nest + 1);
		this.root.createTreeText(sb, nest + 1);
		this.logic.createTreeText(sb, nest + 1);


		return sb;
	}



}
