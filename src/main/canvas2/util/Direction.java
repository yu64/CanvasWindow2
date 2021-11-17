package canvas2.util;

/**
 * 方位を示すクラス<br>
 * 全8方向。基本の4方向のみでループ可能。<br>
 * 斜めは、単位ベクトルではない。
 *
 */
public enum Direction {

	NORTH(0, -1),
	NORTH_WEST(1, -1),
	WEST(1, 0),
	SOUTH_WEST(1, 1),
	SOUTH(0, 1),
	SOUTH_EAST(-1, 1),
	EAST(-1, 0),
	NORTH_EAST(-1, -1),

	;

	private static Direction[] FOUR = {

			NORTH,
			WEST,
			SOUTH,
			EAST,
	};

	public static Direction[] getFour()
	{
		return Direction.FOUR;
	}

	public static boolean isFour(Direction d)
	{
		return (d.ordinal() & 1L) == 0;
	}

	public static int getFourIndex(Direction d)
	{
		if(!Direction.isFour(d))
		{
			return -1;
		}

		return d.ordinal() >> 1;
	}

	public static Direction reverse(Direction d)
	{
		int index = (d.ordinal() + 4) & 0b11;
		return Direction.values()[index];
	}




	private int x;
	private int y;

	private double nX;
	private double nY;

	private Direction(int x, int y)
	{
		this.x = x;
		this.y = y;

		double len = Math.hypot(x, y);
		this.nX = x / len;
		this.nY = y / len;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public double getNormalizedX()
	{
		return this.nX;
	}

	public double getNormalizedY()
	{
		return this.nY;
	}


}
