import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private int gridSize;
	private WeightedQuickUnionUF quickUnion;
	private int[][] siteGrid;
	private int gridLength;
	private int numOpenSites = 0;
	private int virtualTopIndex;
	private int virtualBottomIndex;

	public Percolation(int n) {
		// create n-by-n grid, with all sites blocked
		if (n <= 0) {
			throw new IllegalArgumentException();
		}

		this.gridSize = n;
		gridLength = n * n;
		virtualTopIndex = gridLength;
		virtualBottomIndex = gridLength + 1;

		quickUnion = new WeightedQuickUnionUF(gridLength + 2);
		siteGrid = new int[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				siteGrid[i][j] = 0;
			}
		}
	}

	public void open(int row, int col) {
		// open site (row, col) if it is not open already
		validate(row, col);

		int startSiteIndex = xyTo1D(row, col);
		if (isOpen(row, col)) {
			return;
		}

		siteGrid[row - 1][col - 1] = 1;
		numOpenSites++;

		// if it is a top site, connect to virtual top
		if (row == 1) {
			quickUnion.union(startSiteIndex, virtualTopIndex);
		} else {
			unionComponents(row - 1, col, startSiteIndex);
		}

		// if it is a bottom site, connect to virtual bottom
		if (row == gridSize) {
			quickUnion.union(startSiteIndex, virtualBottomIndex);
		} else {
			unionComponents(row + 1, col, startSiteIndex);
		}

		unionComponents(row, col + 1, startSiteIndex);
		unionComponents(row, col - 1, startSiteIndex);
	}

	private void unionComponents(int x, int y, int startSiteIndex) {
		if (validSite(x, y) && isOpen(x, y)) {
			int neighborSiteIndex = xyTo1D(x, y);
			quickUnion.union(startSiteIndex, neighborSiteIndex);
		}
	}

	private boolean validSite(int row, int col) {
		if ((row <= 0 || col <= 0) || (row > gridSize || col > gridSize)) {
			return false;
		}
		return true;
	}

	public boolean isOpen(int row, int col) {
		validate(row, col);
		if (siteGrid[row - 1][col - 1] == 1) {
			return true;
		}
		return false;
	}

	public boolean isFull(int row, int col) {
		validate(row, col);
		// get to the top
		int index = xyTo1D(row, col);
		return quickUnion.connected(index, virtualTopIndex);
	}

	public int numberOfOpenSites() {
		return numOpenSites;
	}

	public boolean percolates() {
		return quickUnion.connected(virtualTopIndex, virtualBottomIndex);
	}

	private void validate(int x, int y) {
		if ((x <= 0 || y <= 0) || (x > gridSize || y > gridSize)) {
			throw new IndexOutOfBoundsException();
		}
	}

	private int xyTo1D(int x, int y) {
		validate(x, y);
		int index = (gridSize * (y - 1) + x - 1);
		return index;
	}

	public static void main(String[] args) {

	}
}
