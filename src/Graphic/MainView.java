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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import Configuration.Configuration;
import baseLibelle.ExportLibeleBase;
import baseLibelle.ImportTxt;
import baseLibelle.SawtoothList;
import importExcel.Filter;
import importExcel.ReadExcel;
import importExcel.TraitementEtude;

public class MainView {

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
	private void setPath(){
		
	}
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
			}
		});
		btnBaseLibele.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
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
		});
		JButton btnQualificationetudes = new JButton("Qualification Etudes");
		btnQualificationetudes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnQualificationetudes.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnQualificationetudes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
		    	Filter filterMaster = new Filter ();
		    	String conf =  "" ;
		    	try {
					conf = Configuration.getConf(0);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
		    	File[] masters = filterMaster.finder(System.getProperty("user.dir")+"\\MASTER",".docx");
		    	File[] excels  = filterMaster.finder(conf,".xlsx");
		    			
		    	List<TraitementEtude> listEtude = new ArrayList<TraitementEtude>();
		    	for(int i = 0 ; i < masters.length;i++){
		    		try {
		    		boolean canPass =false;
		    		int posExcel=0;
		    		listEtude.add(new TraitementEtude());
		    		listEtude.get(listEtude.size()-1).setEtudeName(masters[i].getName().split("-")[0]);;
		    		listEtude.get(listEtude.size()-1).setQuestion(ReadExcel.importConditionFromWord(masters[i]));
		    		for(int j = 0 ; j < excels.length ; j++){
		    			if(excels[j].getName().split("-")[0].contains(masters[i].getName().split("-")[0])){
		    				try {
								listEtude.get(listEtude.size()-1).setEtudes(ReadExcel.readExcelDocument(excels[j]));
								canPass = true;
								posExcel = j;
		    				} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			    		}
		    		}
		    		if(canPass){
		    			listEtude.get(listEtude.size()-1).checkEtude();
		    			ReadExcel.applyDisqualif(excels[posExcel], listEtude.get(listEtude.size()-1).getEtudes());
		    		}
		    		
		    		}catch (IOException e1) {
						e1.printStackTrace();
					}
	    		}
			}
		});
			
			JButton btnNewButton = new JButton("Import Bases Brutes");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					Filter filtre = new Filter();
					File [] excels = filtre.finder( System.getProperty("user.dir"), ".xlsm");
					//File excelFile = new File("verif base ext + guide.xlsm");
			        ReadExcel.callExcelMacro(excels[0], "!md_selenium.Extract_Site");
			        
			        String sPath = excels[0].getAbsolutePath();
			        sPath = sPath.replace(excels[0].getName(),"");
			        sPath+="Bases\\01. Extractions brutes\\";
			       
		
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			       
			 	   //get current date time with Date()
			
				
			 	  Calendar cal = Calendar.getInstance();
			 	  int month = cal.get(Calendar.MONTH)+1;
			 	  String sDate = cal.get(Calendar.DAY_OF_MONTH) + "." + month +"."+cal.get(Calendar.YEAR);
			
			 	   System.out.println(sDate);
			 	   
			 	   
			 	   
			 	   sPath+=sDate;
			 	   System.out.println(sPath);
			 	  if(!Files.isDirectory(Paths.get(sPath),LinkOption.NOFOLLOW_LINKS)){
			        	File folder = new File (sPath);
			        	folder.mkdir();
			    
			        }
			 	  	try {
						Configuration.setConfig(0, sPath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 	 
			        
			 	  	
			        sPath=sPath.replace("Bases\\01. Extractions brutes\\"+sDate,"BasesQualif");
			      
			        if(!Files.isDirectory(Paths.get(sPath),LinkOption.NOFOLLOW_LINKS)){
			        	File folder = new File (sPath);
			        	folder.mkdir();
			        	try {
							Configuration.setConfig(1, sPath);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
			        sPath = sPath.replace("BasesQualif","BasesLibelle");
			        if(!Files.isDirectory(Paths.get(sPath),LinkOption.NOFOLLOW_LINKS)){
			        	File folder = new File (sPath);
			        	folder.mkdir();
			        	try {
							Configuration.setConfig(3, sPath);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
			        sPath = sPath.replace("BasesLibelle","PrintStudy");
			        if(!Files.isDirectory(Paths.get(sPath),LinkOption.NOFOLLOW_LINKS)){
			        	File folder = new File (sPath);
			        	folder.mkdir();
			        	try {
							Configuration.setConfig(2, sPath);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
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
}
