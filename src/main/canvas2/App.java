package canvas2;

import java.awt.AWTEvent;
import java.awt.Toolkit;

import canvas2.debug.TextTree;
import canvas2.event.EventManager;
import canvas2.event.awt.AwtConnector;
import canvas2.event.awt.AwtDispatcher;
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

	private AwtConnector connector;

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
		this.connector = this.createAwtConnector(this.event);
		this.setup();
	}

	protected AwtConnector createAwtConnector(EventManager event)
	{
		return new AwtConnector(event);
	}

	public void init()
	{
		//初期化
		this.stop();
		this.root.clearChild();
		this.logic.clear();
		this.event.clear();

		Toolkit kit = Toolkit.getDefaultToolkit();
		kit.removeAWTEventListener(this.connector);
	}

	public void setup()
	{
		this.init();

		//描画更新のために、登録
		this.logic.add(this.window);

		//イベント実行のために、登録。
		this.logic.add(this.event);

		//ウィンドウのイベントを扱うディスパッチャーを登録。
		this.event.setDispatcher(AWTEvent.class, new AwtDispatcher());

		//ウィンドウのイベントを中継する。
		Toolkit kit = Toolkit.getDefaultToolkit();
		kit.addAWTEventListener(this.connector, 0xFFFF_FFFF_FFFF_FFFFL);


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
		this.window.createTreeText(sb, nest + 1);

		return sb;
	}



}
