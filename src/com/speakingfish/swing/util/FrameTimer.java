package com.speakingfish.swing.util;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import static java.util.Collections.*;

public class FrameTimer {
    
    protected final JFrame         _owner    ;
    protected final int            _period_ms;
    protected final List<Runnable> _actions  ;
    
    protected volatile boolean _closed = false;
    protected volatile Thread  _thread = null ;
    
    public FrameTimer(JFrame owner, int period_ms, List<Runnable> actions) {
        _owner     = owner    ;
        _period_ms = period_ms;
        _actions   = synchronizedList((null == actions) ? new ArrayList<Runnable>() : new ArrayList<Runnable>(actions));
        needThread();
        _owner.addWindowListener(new WindowListener() {
            
            @Override public void windowOpened(WindowEvent e) {
                _closed = false;
                needThread();
            }
            
            @Override public void windowClosed(WindowEvent e) {
                _closed = true;
                _thread = null;
            }
            
            @Override public void windowIconified  (WindowEvent e) {}
            @Override public void windowDeiconified(WindowEvent e) {}
            @Override public void windowDeactivated(WindowEvent e) {}
            @Override public void windowClosing    (WindowEvent e) {}
            @Override public void windowActivated  (WindowEvent e) {}
        });
    }
    
    public final JFrame         owner    () { return _owner    ; }
    public final int            period_ms() { return _period_ms; }
    public final List<Runnable> actions  () { return _actions  ; }
    
    protected synchronized void needThread() {
        if((null == _thread) && (!_closed) ) {
            //if(_owner.isDisplayable()) {
                _thread = new Thread(new Runnable() {
                    
                    protected final Runnable synchronizedRun = new Runnable() {
                        public void run() {
                            final ArrayList<Runnable> actions;
                            synchronized(_actions) {
                                if(0 == _actions.size()) {
                                    return;
                                }
                                actions = new ArrayList<Runnable>(_actions);
                            }
                            for(final Runnable action : actions) {
                                try {
                                    action.run();
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    
                    public void run() {
                        while(!_closed) {
                            try {
                                Thread.sleep(_period_ms);
                            } catch(InterruptedException e) {
                                e.printStackTrace();
                                break;
                            }
                            synchronized(_actions) {
                                if(0 == _actions.size()) {
                                    continue;
                                }
                            }
                            try {
                                SwingUtilities.invokeAndWait(synchronizedRun);
                            } catch(InvocationTargetException e) {
                                e.printStackTrace();
                            } catch(InterruptedException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                    }
                });
                _thread.start();
            //}
        }
    }

}
