package ui;

import com.mxgraph.model.mxICell;

/**
 * 
 * @author sp611
 *
 */
public class GraphState {
	// TODO: Maybe use observer model to propogate field changes to graph?
	private final mxICell state;
	private boolean isFinal, isStart;

	public GraphState(mxICell state) {
		this(state, false);
	}

	public GraphState(mxICell state, boolean isFinal) {
		super();
		this.state = state;
		this.isStart = false;
		this.isFinal = isFinal;
	}

	public mxICell getState() {
		return state;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

}
