package id.tech.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener mListener;
    private OnItemLongClickListenerRidho mListenerRidho;
    GestureDetector mGestureDetector;
    SimpleOnGestureListener mSimpleGesture;
    private final Context context;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface OnItemLongClickListenerRidho {
        public void onItemLongClickRidho(View view, int position);
    }

    public RecyclerItemClickListener(Context context,
                                     OnItemLongClickListenerRidho listener) {
        final Context ctx = context;
        this.context = context;
        mListenerRidho = listener;
        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

//					@Override
//					public void onLongPress(MotionEvent e) {
//						// TODO Auto-generated method stub
//						return true;

//						Toast.makeText(ctx, "TEST", Toast.LENGTH_SHORT).show();
//						Intent intent = new Intent(ctx, DialogTransfer.class);
//						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						ctx.startActivity(intent);
//					}
                });

    }

//	public RecyclerItemClickListener(Context context,
//			OnItemClickListener listener) {
//		final Context ctx = context;
//		this.context = context;
//		mListener = listener;
//		mGestureDetector = new GestureDetector(context,
//				new GestureDetector.SimpleOnGestureListener() {
//					@Override
//					public boolean onSingleTapUp(MotionEvent e) {
//						return true;
//					}
//
//					@Override
//					public void onLongPress(MotionEvent e) {
//						// TODO Auto-generated method stub
//						super.onLongPress(e);
//						Intent intent = new Intent(ctx, DialogTransfer.class);
//						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						ctx.startActivity(intent);
//					}
//				});
//
//	}

    @Override
    public boolean onInterceptTouchEvent(RecyclerView arg0, MotionEvent e) {
        // TODO Auto-generated method stub
        View childView = arg0.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListenerRidho != null
                && mGestureDetector.onTouchEvent(e)) {
//			mListener.onItemClick(childView, arg0.getChildPosition(childView));
            mListenerRidho.onItemLongClickRidho(childView, arg0.getChildPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView arg0, MotionEvent arg1) {
        // TODO Auto-generated method stub

    }

}
