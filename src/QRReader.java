/*------------------------------------------------------------------------
 *  Copyright 2012 (c) Kiko Qiu <kikoqiu@163.com>
 *
 *  This file is part of the QRReader.
 *
 *  The QRReader is free software; you can redistribute it
 *  and/or modify it under the terms of the GNU Lesser Public License as
 *  published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  The QRReader is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 *  of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with the QRReader; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 *  Boston, MA  02110-1301  USA
 *  http://code.google.com/p/java-qr-code-reader-with-zbar/
 *------------------------------------------------------------------------*/
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.Timer;


import de.humatic.dsj.DSCapture;
import de.humatic.dsj.DSFilterInfo;
import de.humatic.dsj.DSFiltergraph;
import de.humatic.dsj.DSJUtils;
import de.humatic.dsj.rc.RendererControls;


public class QRReader implements java.beans.PropertyChangeListener, ActionListener {

	private DSCapture graph;

	public QRReader() {}

	public void createGraph() {

		javax.swing.JFrame f = new javax.swing.JFrame("qr");

		/** queryDevices returns video device infos in slot 0 / audio device infos in slot 1 **/

		DSFilterInfo[][] dsi = DSCapture.queryDevices();

		/** this sample only uses video **/

		graph = new DSCapture(DSFiltergraph.DD7, dsi[0][0], false, DSFilterInfo.doNotRender(), this);


		f.add(java.awt.BorderLayout.CENTER, graph.asComponent());
		
		
//		f.add(java.awt.BorderLayout.SOUTH, new SwingMovieController(graph));
//
//		final javax.swing.JButton toFile = new javax.swing.JButton("set capture file");
//
//		toFile.addActionListener(new java.awt.event.ActionListener() {
//
//			public void actionPerformed(java.awt.event.ActionEvent e) {
//
//				if (graph.getState() == DSCapture.PREVIEW) {
//
//					/* capture to a Windows Media file using the default profile */
//
//					graph.setCaptureFile("captureTest.asf", DSFilterInfo.doNotRender(), DSFilterInfo.doNotRender(), true);
//
//					toFile.setText("set preview");
//
//					/* start recording right away. Outcomment to control this from GUI */
//
//					graph.record();
//
//				} else {
//
//					graph.setPreview();
//
//					toFile.setText("set capture file");
//
//				}
//
//			}
//
//		});
//
//		f.add(java.awt.BorderLayout.NORTH, toFile);

		f.pack();

		f.setVisible(true);

//		javax.swing.JFrame jf = new javax.swing.JFrame("Device control");
//
//		jf.setLayout(new java.awt.GridLayout(0,1));
//
//		if (graph.getActiveVideoDevice() != null && graph.getActiveVideoDevice().getControls() != null) {
//
//			for (int i = CaptureDeviceControls.BRIGHTNESS; i < CaptureDeviceControls.LT_FINDFACE; i++) try { jf.add(graph.getActiveVideoDevice().getControls().getController(i, 0, true)); }catch (Exception ex){}
//
//		}
//
//		if (graph.getActiveAudioDevice() != null) for (int i = CaptureDeviceControls.MASTER_VOL; i < CaptureDeviceControls.TREBLE; i++) try { jf.add(graph.getActiveAudioDevice().getControls().getController(i, 0, true)); }catch (Exception ex){}
//
//		if (jf.getContentPane().getComponentCount() == 0) return;
//
//		jf.pack();
//
//		jf.setVisible(true);
		
		/**
		Don't do this at home. This demo relies on dsj closing and disposing off filtergraphs when the JVM exits. This is
		OK for a "open graph, do something & exit" style demo, but real world applications should take care of calling
		dispose() on filtergraphs they're done with themselves.
		**/

		f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		
		Timer timer=new Timer(300,this); 
		timer.start();
	}

	public void propertyChange(java.beans.PropertyChangeEvent pe) {

		switch(DSJUtils.getEventType(pe)) {

		}

	}
	
	

	public static void main(String[] args){

		new QRReader().createGraph();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		BufferedImage bi=graph.getImage();
		List<ZBar.Symbol> ls=ZBar.scan(bi);
		if(ls.size()<=0)return;
		
		
		BufferedImage overlay = new BufferedImage(graph.getWidth(), graph.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = overlay.createGraphics();
		g2d.setBackground(new Color(0, 0, 0, 0));
		g2d.setColor(Color.GREEN);
		
		int y0=0;
		for(ZBar.Symbol s:ls){
			System.out.println(s.data);
			g2d.drawPolygon(s.xs, s.ys, s.xs.length);
			g2d.drawString(s.data, 0, y0+=20);
		}
		
		g2d.dispose();
		
		RendererControls rc = graph.getRendererControls();
		rc.setOverlayImage(overlay, null, Color.black, 1);		
	}


}