package Graphic;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.store.Path;

import Configuration.Configuration;
import baseLibelle.ExportLibeleBase;
import baseLibelle.ImportTxt;
import baseLibelle.SawtoothList;
import importExcel.Filter;
import importExcel.ReadExcel;
import importExcel.TraitementEtude;
import importMSQLServer.InformationBDD;
import importMSQLServer.ConnectURL;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.LayoutStyle.ComponentPlacement;

public class MainView {
	static long chrono = 0 ; 
	private JFrame frame;
	/**
	 * Create the application.
	 * @throws PropertyVetoException 
	 */
	public MainView() throws PropertyVetoException {
		initialize();
	}
	public void visible(boolean v){
		this.frame.setVisible(v);
	}
	/**
	 * Initialize the contents of the frame.
	 * @throws PropertyVetoException 
	 */

	private void initialize() throws PropertyVetoException {
		frame = new JFrame();
		
		frame.setResizable(false);
		
		frame.setBounds(100, 100, 571, 202);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		JButton btnBaseLibele = new JButton("Faire bases libell\u00E9es");
		btnBaseLibele.setAlignmentY(Component.TOP_ALIGNMENT);
		btnBaseLibele.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnBaseLibele.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				basesLibeler();
			}
		});
		JButton btnQualificationetudes = new JButton("Qualification Etudes");
	
		btnQualificationetudes.setAlignmentY(Component.BOTTOM_ALIGNMENT);
			btnQualificationetudes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						Go_Chrono();
						LoadingScreen m = new LoadingScreen();
						
						String sPath = "";
						sPath = Configuration.getConf(0);
						  Calendar cal = Calendar.getInstance();
					 	  int month = cal.get(Calendar.MONTH)+1;
					 	  String sDate = ""+cal.get(Calendar.DAY_OF_MONTH)+"." + month +"."+ cal.get(Calendar.YEAR);
						sPath+="\\"+sDate+"\\";
						if(!Files.isDirectory(Paths.get(sPath), LinkOption.NOFOLLOW_LINKS)){
							File folder = new File(sPath);
							folder.mkdir();
							Configuration.setConfig(1,sPath);
						}
						try {
							m.visibility(true);
							basesQualif(m);
							Stop_Chrono();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						m.setVisible(false);
					} catch (InvalidFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			JButton btnCrerMasters = new JButton("Cr\u00E9er masters");
			btnCrerMasters.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					createMaster();
				}
			});
			 
			
			// lProgress.setVisible(false);

			GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
			groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
						.addGap(72)
						.addComponent(btnQualificationetudes)
						.addGap(18)
						.addComponent(btnBaseLibele)
						.addGap(18)
						.addComponent(btnCrerMasters)
						.addContainerGap(114, Short.MAX_VALUE))
			);
			groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
						.addGap(32)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnQualificationetudes)
							.addComponent(btnBaseLibele)
							.addComponent(btnCrerMasters))
						.addContainerGap(97, Short.MAX_VALUE))
			);
			frame.getContentPane().setLayout(groupLayout);
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		JMenu mnNewMenu = new JMenu("Options");
		menuBar.add(mnNewMenu);
		JMenuItem mntmNewMenuItem = new JMenuItem("Configurations");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					OptionView confOption = new OptionView();
					confOption.setVisible(true);		
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
	}
	
	private void basesLibeler(){
		List<String> paths = new ArrayList<String>();
		try {
			paths = Configuration.importConfig();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Filter printStudyFilter = new Filter ();
    	File[] printStudys = printStudyFilter.finder(paths.get(2),".txt");
    	File[] bases = printStudyFilter.finder(paths.get(1),".xlsx");
    	for(int i = 0 ; i < bases.length;i++){
    		List<SawtoothList> sawtoothList = new ArrayList<SawtoothList>();
    		boolean canPass = false;
    		int j=0;
    		for(j=0;j<printStudys.length;j++){
    			String temp = printStudys[j].getName().replaceAll("Print","");
    			temp = temp.replaceAll(".txt", "");
    			if(bases[i].getName().contains(temp)){
    				try {
    					sawtoothList =ImportTxt.getSawtoothList(printStudys[j]);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				canPass = true;
    			}
    		}
    		if(canPass){
    			try {
					ExportLibeleBase.exportBaseWithLibelle(sawtoothList,bases[i]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
    		}
    	}
    	//System.out.println("sortie de libeler");
	}
	static void Go_Chrono() { 
		chrono = java.lang.System.currentTimeMillis() ; 
		} 
	static void Stop_Chrono() { 
		long chrono2 = java.lang.System.currentTimeMillis() ; 
		long temps = chrono2 - chrono ; 
		System.out.println("Temps ecoule = " + temps + " ms") ; 
		} 
	private void basesQualif(LoadingScreen m) throws IOException, InvalidFormatException, InterruptedException{
		m.progressUpdate(0);
		List<TraitementEtude> list = new ArrayList<TraitementEtude>();
    	List<InformationBDD> listBdd = new ArrayList<InformationBDD>();
    	listBdd = ReadExcel.importListBases();
    	int size = 0;
    	System.out.println("début");
    	for(int i = 0 ; i < listBdd.size();i++){
    		size+=listBdd.get(i).getLangues().size();
    	}
    	for(int i = 0 ; i <size; i ++){
    		list.add(new TraitementEtude());
    		
    	}
    	int listIndice = 0;
    	for(int i = 0 ; i < listBdd.size();i++){
    		for(int j = 0 ; j < listBdd.get(i).getLangues().size();j++){
    			list.get(listIndice).setEtudeName(listBdd.get(i).getBase()+listBdd.get(i).getLangues().get(j));
    			list.get(listIndice).setBaseAndLanguage(listBdd.get(i).getLangues().get(j), listBdd.get(i).getBase());
    			listIndice++;
    		}
    	}
    //	System.out.println("mise en place des masters");
    	Filter filterMaster = new Filter ();
    
    	File[] masters = filterMaster.finder(System.getProperty("user.dir")+"\\MASTER",".docx");
    	for(int i = 0 ; i < list.size();i++){
    		for(int j = 0 ; j < masters.length;j++){
    			if(masters[j].getName().contains("-")){
	    			if(list.get(i).getEtudeName().contains(masters[j].getName().split("-")[0])){
	    				list.get(i).setQuestion(ReadExcel.importConditionFromWord(masters[j]));
	    				j=masters.length;
	    			}
    			}
    		}
    	}
		int threadCount = 0;
		System.out.println("size = " +list.size());
		int listSize = list.size();
		int endCount =0;
		int calculCount=0;
		m.progressUpdate(endCount);
		do{
			threadCount = 0;
			for(int i = 0 ; i < list.size(); i ++){
			//	System.out.println(i + "  " + list.get(i).getState());
				if(list.get(i).getState()==Thread.State.TERMINATED){
					list.remove(i);
					i--;
					endCount++;
				}else if(list.get(i).getState()==Thread.State.RUNNABLE){
					threadCount ++;
				}
			}
			if(list.size()==0){
				break;
			}
			if(list.size()>0){
				calculCount = (100/listSize)*endCount;
			}
			//lProgress.setText(endCount+"%");
			if(calculCount != m.getValue()){
				m.progressUpdate(calculCount);
			}
			if(threadCount != 2 ){
				for(int i = 0 ; i < list.size();i++){
				//	System.out.println(i + "  " + list.get(i).getState());
					if(list.get(i).getState()==Thread.State.NEW && threadCount<3){
						threadCount++;
						Thread.currentThread();
						Thread.sleep(5000);
			//			System.out.println("throw new thread " + threadCount);
						list.get(i).start();
						
					}
					
				}
			}
		}while(list.size()!=0);
	}
	private void createMaster(){
		Filter printStudyFilter = new Filter ();
		File[] printStudys=null;
    	try {
			printStudys = printStudyFilter.finder(Configuration.getConf(2),".txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> exportMaster = new ArrayList<String>();
		for(int i = 0 ; i < printStudys.length;i++){
			try {
				exportMaster= ImportTxt.getQuestionList(printStudys[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String temp =printStudys[i].getName();
			temp = temp.replaceAll("[^\\d.]", "");
			temp =temp.replaceAll("\\.","");
			ExportLibeleBase.setMasterWithPrintStudy(exportMaster,"E"+temp+"-");
		}
	}
}

