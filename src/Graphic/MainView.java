package Graphic;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import com.microsoft.sqlserver.jdbc.SQLServerConnection;

import Configuration.Configuration;
import baseLibelle.ExportLibeleBase;
import baseLibelle.ImportTxt;
import baseLibelle.SawtoothList;
import importExcel.Filter;
import importExcel.ReadExcel;
import importExcel.TraitementEtude;
import importMSQLServer.InformationBDD;
import importMSQLServer.ConnectURL;

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
		btnBaseLibele.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				basesLibeler();
			}
			
		});
		JButton btnQualificationetudes = new JButton("Qualification Etudes");
		btnQualificationetudes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Go_Chrono();
					basesQualif();
					Stop_Chrono();
				} catch (InvalidFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnQualificationetudes.setAlignmentY(Component.BOTTOM_ALIGNMENT);

			
			JButton btnNewButton = new JButton("Import Bases Brutes");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Go_Chrono();
					//importBases();
					
					//basesLibeler();
				//	basesQualif();
					Stop_Chrono();
				
				}
				
			});
			GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
			groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
						.addGap(36)
						.addComponent(btnNewButton)
						.addGap(18)
						.addComponent(btnBaseLibele)
						.addGap(18)
						.addComponent(btnQualificationetudes)
						.addContainerGap(121, Short.MAX_VALUE))
			);
			groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
						.addGap(73)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnBaseLibele)
							.addComponent(btnNewButton)
							.addComponent(btnQualificationetudes))
						.addContainerGap(72, Short.MAX_VALUE))
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
    	File[] bases = printStudyFilter.finder(paths.get(0),".xlsx");
    	for(int i = 0 ; i < bases.length;i++){
    		List<SawtoothList> toto = new ArrayList<SawtoothList>();
    		boolean canPass = false;
    		int j=0;
    		for(j=0;j<printStudys.length;j++){
    			String temp = printStudys[j].getName().replaceAll("Print","");
    			temp = temp.replaceAll(".txt", "");
    			if(bases[i].getName().contains(temp)){
    				try {
						toto =ImportTxt.test(printStudys[j]);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				canPass = true;
    			}
    		}
    		if(canPass){
    			try {
					ExportLibeleBase.exportBaseWithLibelle(toto,bases[i]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
    		}
    	}
	}
	static void Go_Chrono() { 
		chrono = java.lang.System.currentTimeMillis() ; 
		} 
	static void Stop_Chrono() { 
		long chrono2 = java.lang.System.currentTimeMillis() ; 
		long temps = chrono2 - chrono ; 
		System.out.println("Temps ecoule = " + temps + " ms") ; 
		} 
	private void basesQualif() throws IOException, InvalidFormatException{
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
    			listIndice++;
    		}
    	}
    	System.out.println("mise en place des masters");
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
    	System.out.println("début import bdd");
		
		listIndice=0;
		int threadCount = 0;
		int [] threadToThrow = new int [list.size()];
		int toThrowCount=0;
		for(int i = 0 ; i < listBdd.size();i++){
			for(int j = 0 ; j < listBdd.get(i).getLangues().size();j++){
				ConnectURL connectionWithDB = new ConnectURL();
				//System.out.println("passage pour : " + list.get(listIndice).getEtudeName());
				list.get(listIndice).setEtudes(connectionWithDB.test(listBdd.get(i).getBase(), listBdd.get(i).getLangues().get(j)));
				if(threadCount<2){
					list.get(listIndice).start();
					list.get(listIndice).setPriority(7);
					threadCount++;
				} else {
					threadToThrow[toThrowCount] = listIndice;
					toThrowCount++;
				}
				listIndice++;
				
				
			}
		}
		System.out.println("test + " + toThrowCount);
		for(int i = 0 ; i < list.size(); i ++){
			System.out.println(i + "  " + list.get(i).getState());
		}
		listIndice = 0;
		do{
			threadCount = 0;
			for(int i = 0 ; i < list.size(); i ++){
			//	System.out.println(i + "  " + list.get(i).getState());
				if(list.get(i).getState()==Thread.State.RUNNABLE){
					threadCount ++;
				}
			}
			for(int i = 0 ; i < toThrowCount;i++){
			//	System.out.println(i + "  " + list.get(i).getState());
				if(list.get(threadToThrow[i]).getState()==Thread.State.NEW && threadCount<2){
					list.get(threadToThrow[i]).start();
					threadCount++;
				}
				
			}
		}while(threadCount!=0);
	}
	
    
}

