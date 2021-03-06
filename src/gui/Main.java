package gui;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JLabel;
import javax.swing.Timer;

import javax.swing.ButtonGroup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import java.awt.Font;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import urlReader.UrlHandler;
import javax.swing.JMenuItem;
import urlReader.UrlResendHandler;

public class Main {

	private JFrame frame;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private JTextField domainField;
	private JTextField roomField;
	private JTextField configField;
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	private Thread urlThread;
        private JLabel statusLabel;
	private int broj = 10;
	private Timer timer;
	private Timer timer1;
	private JButton btnStop;
        UrlResendHandler resender = new UrlResendHandler();
        
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
        
        private enum Actions {
            START,
            STOP,
            RESEND
        }
        
	private void initialize() {
                //UrlResendHandler resender;
                //UrlResendHandler resender;
		initFiles();
		frame = new JFrame();
		frame.setTitle(Messages.getString("Main.frmAudienceResponseSystem.title")); //$NON-NLS-1$
		frame.setBounds(200, 100, 300, 470);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

                int y_offset = 20;
                
		JLabel lblNewLabel_domain = new LocLabel("Main.lblNewLabel.text");
		lblNewLabel_domain.setText(Messages.getString("Main.lblNewLabel.text")); //$NON-NLS-1$
		lblNewLabel_domain.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_domain.setBounds(30, y_offset, 150, 30);
		panel.add(lblNewLabel_domain);

                domainField = new JTextField();
		domainField.setBounds(110, y_offset + 4, 140, 25);
		domainField.setText(Messages.getString("Main.rdbtnHttpwwwauressorg.text"));
                domainField.setColumns(8);
		panel.add(domainField);
		
                JLabel lblNewLabel_1 = new LocLabel("Main.lblNewLabel_1.text"); //$NON-NLS-1$
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(30, y_offset + 30, 315, 30);
		panel.add(lblNewLabel_1);

		roomField = new JTextField();
		roomField.setText("");
		roomField.setBounds(110, y_offset + 34, 35, 25);
                roomField.setColumns(5);
		panel.add(roomField);
		
                Scanner s;
		try {
			s = new Scanner(Paths.get("settings.txt"));
                        String domena = "";
                        
                        // Ucitaj custom domenu
                        if (s.hasNextLine()) {
                            domena = s.nextLine();
                        }
                        
                        // Provjeri je li domena defaultna ili custom zadana
                        /*if (domena.equals("http://www.auress.org/") || domena.equals("")) {
				defDomain.doClick();
			} else {
				cusDomain.doClick();
				domainField.setText(domena);
			}*/
                        domainField.setText(domena);
                        
			s.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JSeparator separator = new JSeparator();
		separator.setBounds(10, y_offset + 75, 260, 2);
		panel.add(separator);

                JLabel label = new LocLabel("Main.label.text"); //$NON-NLS-1$
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		label.setBounds(30, 110, 118, 20);
		panel.add(label);
                
                final JComboBox<String> comboOutput = new JComboBox<String>();
                comboOutput.addItem(Messages.getString("Main.btnPlainText.text"));
                comboOutput.addItem(Messages.getString("Main.btnFindConfigFile.text"));
                comboOutput.addItem(Messages.getString("Main.btnNewButton.text"));
                comboOutput.setBounds(30, 150, 118, 20);
                panel.add(comboOutput);
                
                comboOutput.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        JComboBox<String> combo = (JComboBox<String>) event.getSource();
                        String selectedOutput = (String) combo.getSelectedItem();
                        
                        //ItemCount provjeravam jer je on jednak nuli u trenutku kad resetiram Iteme i pozove se action listener
                        if ((comboOutput.getItemCount()>0) && (selectedOutput.equals(Messages.getString("Main.btnFindConfigFile.text")))) {
                              final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						Scanner sc = new Scanner(file);
						PrintWriter pw = new PrintWriter("tempConfig.txt");
						while (sc.hasNextLine()) {
							pw.write(sc.nextLine() + System.getProperty("line.separator"));
						}
						sc.close();
						pw.close();
						String[] arr = file.toString().split("\\\\");
						configField.setText(arr[arr.length - 1]);
						//configFile.doClick();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
                                        ;
                                } else if (returnVal == JFileChooser.CANCEL_OPTION) { //Ako je kliknuo cancel onda vrati na plain text
                                    comboOutput.setSelectedItem(Messages.getString("Main.btnPlainText.text"));                                    
                                }
                        } else if ((comboOutput.getItemCount()>0) && (selectedOutput.equals(Messages.getString("Main.btnNewButton.text")))) {
                                comboOutput.setSelectedItem(Messages.getString("Main.btnPlainText.text"));                                    
                                new NewConfig();                                
                        } else if ((comboOutput.getItemCount()>0) && (selectedOutput.equals("Plain text"))) {
                        	configField.setText(Messages.getString("Main.textField.text"));
                        }
                        
                    }
                });

                configField = new JTextField();
                configField.setEditable(false);
		configField.setText(Messages.getString("Main.textField.text"));
		configField.setBounds(30, 170, 119, 20);
                configField.setColumns(10);
		panel.add(configField);
                
		final JCheckBox userId = new LocCheckBox("Main.chckbxSenderId.text"); //$NON-NLS-1$
		userId.setSelected(true);
		userId.setBounds(180, 148, 97, 23);
		panel.add(userId);

		final JCheckBox message = new LocCheckBox("Main.chckbxMessage.text"); //$NON-NLS-1$
		message.setSelected(true);
		message.setBounds(180, 168, 97, 23);
		panel.add(message);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, y_offset + 200 , 260, 2);
		panel.add(separator_1);

		JLabel lblInputControl = new LocLabel("Main.lblInputControl.text"); //$NON-NLS-1$
		lblInputControl.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblInputControl.setBounds(30, y_offset + 210, 154, 30);
		//panel.add(lblInputControl);

                y_offset = y_offset - 30;
                
		ActionListener al_big = new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    
                    String soba = roomField.getText();
                    String domena = proofDomain(domainField.getText());
                    
                    // Ako ne prolazi validacija postavki povezivanja odmah umri
                    if (!checkRoom(soba) || !check_domain_room(soba, domena))
                        return;
                    
                    if (arg0.getActionCommand().equals("TimerCommand") || (arg0.getActionCommand().equals("ResendTimerCommand"))) {
                        statusLabel.setText(Messages.getString("Main.statusLabel1")+" "+broj+" "+ Messages.getString("Main.statusLabel2"));
                        broj--;
                        if (broj == -1) {
                            timer.stop();
                            broj = 10;
                            if (arg0.getActionCommand().equals("TimerCommand")) {
                                statusLabel.setText(Messages.getString("Main.statusLabel3"));
                                urlThread.start();
                            } else {
                                statusLabel.setText(Messages.getString("Main.currentlyResending"));
                                resender.run();
                                statusLabel.setText(Messages.getString("Main.lblToInitiate.text"));
                            }
                        }
                        return;
                    }
                    
                    // Provjeri mogu li se otvorit pomocne txt datoteke i postavi connect_to varijablu
                    String connect_to = "";
                    if (arg0.getActionCommand() == Actions.RESEND.name()) {
                        try {
                            PrintWriter pw = new PrintWriter("newMessage.txt");
                            pw.close();
                            pw = new PrintWriter("oldMessage.txt");
                            pw.close();
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        connect_to = "/studentMessages.txt";
                    } else {
                        connect_to = "/zadnjiOdgovor.txt";
                    }
                    
                    // Napisi settings.txt datoteku s postavkama outputa
                    try {
                        PrintWriter pw = null;
                        pw = new PrintWriter("settings.txt");
                        pw.write(domena);
                    
                        String nl = System.getProperty("line.separator");
                        if (comboOutput.getSelectedItem().equals(Messages.getString("Main.btnPlainText.text"))) {pw.write(nl + "Plain");}
                        else {pw.write(nl + "Config");}
                    
                        if (userId.isSelected()) {pw.write(nl + "UserId");}
                        else {pw.write(nl + "NoUserId");}
                    
                        if (message.isSelected()) {pw.write(nl + "Message");}
                        else {pw.write(nl + "NoMessage");}
                        pw.close();
                    
                    } catch (FileNotFoundException e) {
                        JOptionPane.showMessageDialog(null,Messages.getString("Main.settingsError"));
                        return;
                    }
                    
                    // Spoji se i pocni odbrojavanje
                    try {
                    
                        URL myUrl = new URL(domena + soba + connect_to);
                        InputStream input = myUrl.openStream();
                        timer.start();
                        if (arg0.getActionCommand() == Actions.RESEND.name()) {
                                resender = new UrlResendHandler(myUrl);
                                timer.setActionCommand("ResendTimerCommand");
                        } else if (arg0.getActionCommand() == Actions.START.name()) {
                                urlThread = new Thread(new UrlHandler(myUrl));
                                timer.setActionCommand("TimerCommand");                                
                        }
                    } catch (MalformedURLException e2) {
                    } catch (IOException ex) {}
                }
            };
		
		//timer = new Timer(1000, al);
                timer = new Timer(1000, al_big);
		//timer1 = new Timer(1000, al1);

                //http://stackoverflow.com/questions/5936261/how-to-add-action-listener-that-listens-to-multiple-buttons
                
		JButton btnStart = new LocButton("Main.btnStart.text"); //$NON-NLS-1$
		btnStart.setActionCommand(Actions.START.name());
                btnStart.addActionListener(al_big);
		btnStart.setBounds(35+5, y_offset + 250, 89, 23);
		panel.add(btnStart);

		btnStop = new LocButton("Main.btnStop.text"); //$NON-NLS-1$
                btnStop.setActionCommand(Actions.STOP.name());
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UrlHandler.setGotovo();
				if (timer.isRunning()) {
                                    timer.stop();
                                    broj = 10;
                                }
                                statusLabel.setText(Messages.getString("Main.lblToInitiate.text"));
				
			}
		});
		btnStop.setBounds(150+5, y_offset + 250, 89, 23);
		panel.add(btnStop);

		JButton btnResend = new LocButton("Main.btnResend.text"); //$NON-NLS-1$
		btnResend.setActionCommand(Actions.RESEND.name());
                btnResend.setBounds(85+5, y_offset + 290, 100, 25);
		btnResend.addActionListener(al_big);
		panel.add(btnResend);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(10, y_offset + 340, 260, 2);
		panel.add(separator_2);

		//JLabel statusLabel = new LocLabel(Messages.getString("Main.lblToInitiate.text")); //$NON-NLS-1$
                statusLabel = new JLabel(Messages.getString("Main.lblToInitiate.text")); //$NON-NLS-1$
                //statusLabel = new JLabel(Messages.getString("Main.lblToInitiate.text")); //$NON-NLS-1$
                statusLabel.setFont(new Font("Tahoma", 2, 12));
                statusLabel.setText(Messages.getString("Main.lblToInitiate.text"));
                //statusLabel.setText(Messages.getString("Main.lblToInitiate.text"));
		statusLabel.setBounds(30+5, y_offset + 360, 260, 30);
		panel.add(statusLabel);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(Box.createHorizontalGlue());
		frame.setJMenuBar(menuBar);

		JMenu mnLanguage = new LocMenu("Main.mnLanguage.text");
		menuBar.add(mnLanguage);

		JRadioButtonMenuItem rdbtnmntmCroatian = new JRadioButtonMenuItem(
				Messages.getString("Main.rdbtnmntmCroatian.text"));
		rdbtnmntmCroatian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Locale.setDefault(new Locale("hr", "HR"));
				Messages.changeBundle();
				Messages.fire();
                                // Ovo ne bi trebalo ic tu ali iz nekog razloga mora;
                                configField.setText(Messages.getString("Main.textField.text"));
                                comboOutput.removeAllItems();
                                comboOutput.addItem(Messages.getString("Main.btnPlainText.text"));
                                comboOutput.addItem(Messages.getString("Main.btnFindConfigFile.text"));
                                comboOutput.addItem(Messages.getString("Main.btnNewButton.text"));
                                statusLabel.setText(Messages.getString("Main.lblToInitiate.text"));
                                
			}
		});
		buttonGroup.add(rdbtnmntmCroatian);
		mnLanguage.add(rdbtnmntmCroatian);

		JRadioButtonMenuItem rdbtnmntmEnglish = new JRadioButtonMenuItem(
				Messages.getString("Main.rdbtnmntmEnglish.text")); //$NON-NLS-1$
		rdbtnmntmEnglish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Locale.setDefault(new Locale("en", "US"));
				Messages.changeBundle();
				Messages.fire();
                                // Ovo ne bi trebalo ic tu ali iz nekog razloga mora;
                                configField.setText(Messages.getString("Main.textField.text"));
                                statusLabel.setText(Messages.getString("Main.lblToInitiate.text"));
                                comboOutput.removeAllItems();
                                comboOutput.addItem(Messages.getString("Main.btnPlainText.text"));
                                comboOutput.addItem(Messages.getString("Main.btnFindConfigFile.text"));
                                comboOutput.addItem(Messages.getString("Main.btnNewButton.text"));
                
			}
		});
		rdbtnmntmEnglish.setSelected(true);
		buttonGroup.add(rdbtnmntmEnglish);
		mnLanguage.add(rdbtnmntmEnglish);

		JMenu mnAbout = new LocMenu("Main.mnAbout.text"); //$NON-NLS-1$
		menuBar.add(mnAbout);

		JMenuItem mntmNewMenuItem = new LocMenuItem(Messages.getString("Main.mntmNewMenuItem.text")); //$NON-NLS-1$
                mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//new About();
                            
                                JPanel p = new JPanel(new java.awt.GridLayout(0, 1));
                                JLabel about_content = new JLabel();
                                p.setSize(470, 430);
                                about_content.setBounds(10, 10, 450, 410);
                                about_content.setText(Messages.getString("Main.mnAbout.content"));
                                p.add(about_content);
                                
                                JFrame about_window = new About();
                                about_window.setSize(470,480);
                                //about_window.getContentPane().setBackground(Color.DARK_GRAY);
                                about_window.setTitle(Messages.getString("Main.mntmNewMenuItem.text"));
                                about_window.setContentPane(p);
                        }
		});
		mnAbout.add(mntmNewMenuItem);
	}

	public void initFiles() {
		File f = new File(Paths.get("settings.txt").toString());
		try {
			if (!f.isFile()) {
				PrintWriter pw = new PrintWriter("settings.txt");
				pw.write("http://www.auress.org/");
				pw.close();
			}
			//Runtime.getRuntime().exec("attrib +H settings.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		f = new File(Paths.get("tempConfig.txt").toString());
		f.deleteOnExit();
		try {
			if (!f.isFile()) {
				PrintWriter pw = new PrintWriter("tempConfig.txt");
				pw.close();
			}
			//Runtime.getRuntime().exec("attrib +H tempConfig.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		f = new File(Paths.get("oldMessage.txt").toString());
		f.deleteOnExit();
		try {
			if (!f.isFile()) {
				PrintWriter pw = new PrintWriter("oldMessage.txt");
				pw.close();
			}
			//Runtime.getRuntime().exec("attrib +H oldMessage.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		f = new File(Paths.get("newMessage.txt").toString());
		f.deleteOnExit();
		try {
			if (!f.isFile()) {
				PrintWriter pw = new PrintWriter("newMessage.txt");
				pw.close();
			}
			//Runtime.getRuntime().exec("attrib +H newMessage.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
        
        public boolean checkRoom(String soba) {
        
            if ((soba.length()==4) && (soba.substring(0, 1).matches("[0-9]")) && (soba.substring(1, 2).matches("[0-9]")) && (soba.substring(2, 3).matches("[0-9]")) && (soba.substring(3, 4).matches("[0-9]"))) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null,Messages.getString("Main.roomError"));
                return false;
            }
            
        }
        
        public boolean check_domain_room (String room, String domain) {
            
            try {
                URL myUrl = new URL(domain + room + "/studentMessages.txt");
                try {
                    InputStream input = myUrl.openStream();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,Messages.getString("Main.urlError"));
                    return false;
                }
                return true;
            }
            catch (MalformedURLException e) {
                    JOptionPane.showMessageDialog(null,Messages.getString("Main.urlError"));
                    return false;
            }
        }
        
        public String proofDomain(String domena) {
        
            if (!domena.startsWith("http://"))
                domena = "http://" + domena;
            if (!domena.endsWith("/"))
                domena = domena + "/";
            return domena;
        }

}
