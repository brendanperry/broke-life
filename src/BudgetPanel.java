import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/*
 * This class contains the GUI for the Budget Panel and allows 
 * users to enter in transactions and income. From here, the user
 * can add, modify, and delete events. The event data will be loaded
 * from the user profile.
 * @author brendanperry
 * 03/20/20
 */

@SuppressWarnings("serial")
public class BudgetPanel extends JPanel {
	
	// these public fields must be accessed by action handlers
	
	UserProfile user;
	
	// table fields
	DefaultTableModel model;
	JTable table;
	ArrayList<String[]> data;
	
	// review panel fields
	JTextField totalIncome;
	JTextField totalBudgeted;
	JTextField leftToBudget;
	
	// income panel fields
	JTextField payOne;
	JTextField payTwo;
	JTextField payThree;
	JTextField payFour;
	JTextField miscOne;
	JTextField miscTwo;
	JTextField miscThree;
	JTextField miscFour;
	JTextField tips;
	
	// event panel fields
	JTextField name;
	JTextField cost;
	JTextField day;
	JTextField percentage;
	@SuppressWarnings("rawtypes")
	JComboBox repeating;
	JTextField category;
	JButton submitChanges;
	JComboBox<String> eventComboBox;
	
	// current selected month and year
	static int tableYear = 2020;
	static int tableMonth = 4;
	
	// the sum of all user income for the month
	double sum = 0.0;
	
	// money left to be budgeted
	static double left = 0.0;
	
	/*
	 * This is the constructor and is in control of all the user interface.
	 * @params userProfile - this lets the budget panel get data about the user and save new data
	 * @author brendanperry
	 */
	public BudgetPanel(UserProfile userProfile) {
		setLayout(new BorderLayout(0, 0));
		
		user = userProfile;
		
		data = new ArrayList<String[]>();
		
		Color bgColor = Color.decode("#3e92cc");
		Color foregroundColor = Color.decode("#25ced1");
		
		// These are the two classes that handle user input 
		ActionHandler actionHandler = new ActionHandler();
		FocusHandler focusHandler = new FocusHandler();
		
		// EXPENSES PANEL
		
		JPanel expensesPanel = new JPanel();
		expensesPanel.setSize(new Dimension(300, 300));
		expensesPanel.setLayout(new BorderLayout(0, 0));
		expensesPanel.setBackground(bgColor);
		expensesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JLabel expensesText = new JLabel("Expenses", SwingConstants.CENTER);
		expensesText.setForeground(Color.WHITE);
		expensesText.setFont(new Font("Arial", Font.BOLD, 20));
		expensesPanel.add(expensesText, BorderLayout.NORTH);
						
		// Disables the users ability to edit individual cells
		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};	
		
		table = new JTable(model);
		table.getTableHeader().setBackground(foregroundColor);
		model.addColumn("Item");
		model.addColumn("Cost");
		model.addColumn("Percentage");
		model.addColumn("Day");
		model.addColumn("Repeating");
		model.addColumn("Category");
		
		// The table is in a scroll pane so more events than can fit in the pane can be added
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.getViewport().setBackground(Color.WHITE);
		expensesPanel.add(scrollPane, BorderLayout.CENTER);
		
		// RIGHT PANEL
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout(0, 0));
		
		// INCOME PANEL
		
		JPanel incomePanel = new JPanel();
		incomePanel.setBackground(bgColor);
		incomePanel.setPreferredSize(new Dimension(300, 300));
		incomePanel.setLayout(new BorderLayout());
		incomePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JPanel payPanel = new JPanel();
		payPanel.setLayout(new GridLayout(5, 1));
		payPanel.setBackground(bgColor);
		JPanel miscPanel = new JPanel(new GridLayout(5, 1));
		miscPanel.setBackground(bgColor);
		
		JLabel incomeText = new JLabel("Income", SwingConstants.CENTER);
		incomeText.setForeground(Color.WHITE);
		incomeText.setFont(new Font("Arial", Font.BOLD, 20));
		incomePanel.add(incomeText, BorderLayout.NORTH);
		
		JLabel payText = new JLabel("PAY");
		payPanel.add(payText);
		payText.setForeground(Color.WHITE);
		payText.setFont(new Font("Arial", Font.BOLD, 13));
		
		payOne = new JTextField();
		payOne.setPreferredSize(new Dimension(125, 50));
		payOne.addActionListener(actionHandler);
		payOne.addFocusListener(focusHandler);
		
		payTwo = new JTextField();
		payTwo.setPreferredSize(new Dimension(125, 50));
		payTwo.addActionListener(actionHandler);
		payTwo.addFocusListener(focusHandler);
		
		payThree = new JTextField();
		payThree.setPreferredSize(new Dimension(125, 50));
		payThree.addActionListener(actionHandler);
		payThree.addFocusListener(focusHandler);
		
		payFour = new JTextField();
		payFour.setPreferredSize(new Dimension(125, 50));
		payFour.addActionListener(actionHandler);
		payFour.addFocusListener(focusHandler);
		
		payPanel.add(payOne);
		payPanel.add(payTwo);
		payPanel.add(payThree);
		payPanel.add(payFour);
		
		incomePanel.add(payPanel, BorderLayout.WEST);
		
		JLabel miscText = new JLabel("MISC");
		miscPanel.add(miscText);
		miscText.setForeground(Color.WHITE);
		miscText.setFont(new Font("Arial", Font.BOLD, 13));
		
		miscOne = new JTextField();
		miscOne.setPreferredSize(new Dimension(125, 50));
		miscOne.addActionListener(actionHandler);
		miscOne.addFocusListener(focusHandler);
		
		miscTwo = new JTextField();
		miscTwo.setPreferredSize(new Dimension(125, 50));
		miscTwo.addActionListener(actionHandler);
		miscTwo.addFocusListener(focusHandler);
		
		miscThree = new JTextField();
		miscThree.setPreferredSize(new Dimension(125, 50));
		miscThree.addActionListener(actionHandler);
		miscThree.addFocusListener(focusHandler);
		
		miscFour = new JTextField();
		miscFour.setPreferredSize(new Dimension(125, 50));
		miscFour.addActionListener(actionHandler);
		miscFour.addFocusListener(focusHandler);
		
		miscPanel.add(miscOne);
		miscPanel.add(miscTwo);
		miscPanel.add(miscThree);
		miscPanel.add(miscFour);
		
		incomePanel.add(miscPanel, BorderLayout.EAST);
		
		JPanel tipsPanel = new JPanel();
		tipsPanel.setBackground(bgColor);
		tipsPanel.setLayout(new BorderLayout());
		
		JLabel tipsText = new JLabel("TIPS");
		tipsText.setForeground(Color.WHITE);
		tipsText.setFont(new Font("Arial", Font.BOLD, 13));
		tipsPanel.add(tipsText, BorderLayout.NORTH);
		
		tips = new JTextField();
		tips.setPreferredSize(new Dimension(300, 40));
		tips.addActionListener(actionHandler);
		tips.addFocusListener(focusHandler);
		tipsPanel.add(tips, BorderLayout.SOUTH);
		
		incomePanel.add(tipsPanel, BorderLayout.SOUTH);
				
		// REVIEW PANEL
		
		JPanel reviewPanel = new JPanel();
		reviewPanel.setPreferredSize(new Dimension(300,300));
		reviewPanel.setLayout(new GridLayout(7, 1));
		reviewPanel.setBackground(bgColor);
		reviewPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JLabel reviewText = new JLabel("Review", SwingConstants.CENTER);
		reviewText.setForeground(Color.WHITE);
		reviewText.setFont(new Font("Arial", Font.BOLD, 20));
		
		JLabel totalIncomeText = new JLabel("TOTAL INCOME");
		totalIncomeText.setFont(new Font("Arial", Font.BOLD, 13));
		totalIncomeText.setForeground(Color.WHITE);
		totalIncome = new JTextField("$0.00");
		totalIncome.setEditable(false);
		totalIncome.setBackground(Color.WHITE);
		
		JLabel totalBudgetedText = new JLabel("TOTAL BUDGETED");
		totalBudgetedText.setFont(new Font("Arial", Font.BOLD, 13));
		totalBudgetedText.setForeground(Color.WHITE);
		totalBudgeted = new JTextField("$0.00");
		totalBudgeted.setEditable(false);
		totalBudgeted.setBackground(Color.WHITE);
		
		JLabel leftToBudgetText = new JLabel("MONEY LEFT");
		leftToBudgetText.setFont(new Font("Arial", Font.BOLD, 13));
		leftToBudgetText.setForeground(Color.WHITE);
		leftToBudget = new JTextField("$0.00");
		leftToBudget.setEditable(false);
		leftToBudget.setBackground(Color.WHITE);
		
		reviewPanel.add(reviewText);
		reviewPanel.add(totalIncomeText);
		reviewPanel.add(totalIncome);
		reviewPanel.add(totalBudgetedText);
		reviewPanel.add(totalBudgeted);
		reviewPanel.add(leftToBudgetText);
		reviewPanel.add(leftToBudget);
		
		rightPanel.add(incomePanel, BorderLayout.CENTER);
		rightPanel.add(reviewPanel, BorderLayout.SOUTH);
		rightPanel.setSize(300, 300);
		
		// EVENT PANEL
		
		JPanel eventPanel = new JPanel();
						
		name = new JTextField();
		name.setPreferredSize(new Dimension(100,30));
		cost = new JTextField("0");
		cost.setPreferredSize(new Dimension(100,30));
		percentage = new JTextField("0");
		percentage.setPreferredSize(new Dimension(100,30));
		day = new JTextField("1");
		day.setPreferredSize(new Dimension(100,30));
		String[] interactionsRepeating = {"None", "Daily", "Weekly", "Biweekly", "Monthly", "Yearly"};
		repeating = new JComboBox<String>(interactionsRepeating);
		category = new JTextField("Misc");
		category.setPreferredSize(new Dimension(100,30));
		
		JLabel nameText = new JLabel("Name");
		nameText.setFont(new Font("Arial", Font.BOLD, 13));
		JLabel costText = new JLabel("Cost");
		costText.setFont(new Font("Arial", Font.BOLD, 13));
		JLabel percentageText = new JLabel("Percentage");
		percentageText.setFont(new Font("Arial", Font.BOLD, 13));
		JLabel dayText = new JLabel("Day");
		dayText.setFont(new Font("Arial", Font.BOLD, 13));
		JLabel repeatingText = new JLabel("Repeating");
		repeatingText.setFont(new Font("Arial", Font.BOLD, 13));
		JLabel categoryText = new JLabel("Category");
		categoryText.setFont(new Font("Arial", Font.BOLD, 13));
		
		eventPanel.setBackground(foregroundColor);
		
		eventPanel.add(nameText);
		eventPanel.add(name);
		eventPanel.add(costText);
		eventPanel.add(cost);
		eventPanel.add(percentageText);
		eventPanel.add(percentage);
		eventPanel.add(dayText);
		eventPanel.add(day);
		eventPanel.add(repeatingText);
		eventPanel.add(repeating);
		eventPanel.add(categoryText);
		eventPanel.add(category);
				
		String[] interactions = {"Add Event", "Modify Event", "Delete Event"};
		eventComboBox = new JComboBox<String>(interactions);
		eventComboBox.addActionListener(actionHandler);
		eventComboBox.setBackground(foregroundColor);
		eventPanel.add(eventComboBox);
		
		submitChanges = new JButton("Add Row");
		submitChanges.setForeground(Color.WHITE);
		submitChanges.setBackground(bgColor);
		submitChanges.addActionListener(actionHandler);		
		eventPanel.add(submitChanges);
		
		add(expensesPanel, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);
		add(eventPanel, BorderLayout.SOUTH);
		
		try {
			LocalDate date = LocalDate.now();
			Helper helper = new Helper();
			helper.loadUserData(date.getMonthValue(), date.getYear());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * This function grabs data from the month header
	 * @params month is the selected month and year is the selected year
	 * @author brendanperry
	 */
	public void loadFromHeader(int month, int year) throws ParseException {
		Helper helper = new Helper();
		helper.loadUserData(month, year);
	}
	
	/*
	 * The FocusHandler is used to see when the user has entered in text into the
	 * income section of the GUI. This is used to format the text when they are done
	 * and update other values.
	 * @author brendanperry
	 */
	public class FocusHandler implements FocusListener {
		
		Helper helper = new Helper();
		
		/*
		 * focusLost fires when the user clicks out of one of the text boxes on the 
		 * income panel
		 * @params e is the focus event that can be used to see which text box called
		 * the function
		 * @author brendanperry
		 */
		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			
			if(e.getSource() == payOne) {				
				helper.updateStats(payOne, 0, 0);
			}
			else if(e.getSource() == payTwo) {
				helper.updateStats(payTwo, 0, 1);
			}
			else if(e.getSource() == payThree) {
				helper.updateStats(payThree, 0, 2);
			}
			else if(e.getSource() == payFour) {
				helper.updateStats(payFour, 0, 3);
			}
			else if(e.getSource() == tips) {
				helper.updateStats(tips, 2, 0);
			}
			else if(e.getSource() == miscOne) {
				helper.updateStats(miscOne, 1, 0);
			}
			else if(e.getSource() == miscTwo) {
				helper.updateStats(miscTwo, 1, 1);
			}
			else if(e.getSource() == miscThree) {
				helper.updateStats(miscThree, 1, 2);
			}
			else if(e.getSource() == miscFour) {
				helper.updateStats(miscFour, 1, 3);
			}
		}
		
		/*
		 * focusGained is not used but required by Java
		 */
		@Override
		public void focusGained(FocusEvent e) {
		}
	}
	
	/*
	 * ActionHandler is responsible for the Add, Modify, and Delete event button.
	 * From here, the data will be updated based on the value of the button at the time.
	 * It is also used for the combo boxes for swapping between add, modify, and delete and for
	 * changing the repeating frequency.
	 * @author brendanperry
	 */
	public class ActionHandler implements ActionListener {
		
		Helper helper = new Helper();
		
		/*
		 * actionPerformed is called when the button or a combo box is clicked.
		 * @params e provides action event data that can be used to see what called the function.
		 * @author brendanperry
		 */
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == submitChanges) {
				
				if(eventComboBox.getSelectedIndex() == 0) {
					// add event
					String[] newData = {name.getText(), cost.getText(), percentage.getText(), day.getText(), repeating.getSelectedItem().toString(), category.getText()};
					String[] checkedData = helper.checkDataValidity(newData);
					
					if(checkedData != null) {				
						String date = day.getText();
						
						int repeat = helper.recurStringToInt(repeating.getSelectedItem().toString());
						double recurTotalCost = helper.currencyToDouble(cost.getText());
						
						if(repeat != 0 && repeat != 365 && repeat != 30) {
							// find last day of the selected month
							int finalDay = 0;
							if(tableMonth == 1 || tableMonth == 3 || tableMonth == 5 || tableMonth == 7 || tableMonth == 8 || tableMonth == 10 || tableMonth == 12) {
								// last day is 31st
								finalDay = 31;
							}
							else if(tableMonth == 2) {
								// last day is 28th
								finalDay = 28;
							}
							else {
								// last day is 30th
								finalDay = 30;
							}
							
							// calculate the cost of recurring events over the whole month
							recurTotalCost = 0;
							for(int j = Integer.parseInt(date); j <= finalDay; j+= repeat) {
								recurTotalCost += helper.currencyToDouble(cost.getText());
							}
						}
						
						totalBudgeted.setText(helper.formatCurrency(helper.currencyToDouble(totalBudgeted.getText()) + recurTotalCost));
						user.getIncome(tableYear, tableMonth).setBalance(sum - helper.currencyToDouble(totalBudgeted.getText()));
						leftToBudget.setText(helper.formatCurrency(sum - helper.currencyToDouble(totalBudgeted.getText())));
						
						data.add(checkedData);
						model.addRow(checkedData);
						
						try {
							helper.saveUserData(checkedData);
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						name.setText("");
						cost.setText("0");
						percentage.setText("0");
						day.setText("1");
						repeating.setSelectedIndex(0);
						category.setText("Misc");
					}
				}
				else if(eventComboBox.getSelectedIndex() == 1) {
					// modify event
					if(model.getRowCount() == 0) {
						return;
					}
					
					String[] dataToModify = {name.getText(), cost.getText(), percentage.getText(), day.getText(), repeating.getSelectedItem().toString(), category.getText()};
					String[] checkedData = helper.checkDataValidity(dataToModify);
					
					if(checkedData != null) {
						int row = table.getSelectedRow();
						
						if(row != -1) {						
							String title = data.get(row)[0];
							double costCurrent = helper.currencyToDouble(data.get(row)[1]);
							int percent = Integer.parseInt(data.get(row)[2]);
							int dayCurrent = Integer.parseInt(data.get(row)[3]);
							int repeat = helper.recurStringToInt(data.get(row)[4]);
							String tag = data.get(row)[5];
							
							DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							String dateString = dayCurrent + "/" + tableMonth + "/" + tableYear;
							String newDateString = checkedData[3] + "/" + tableMonth + "/" + tableYear;
							
							Date dateObject = new Date();
							Date newDateObject = new Date();
							
							try {
								dateObject = sdf.parse(dateString);
								newDateObject = sdf.parse(newDateString);
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							int indexToModify = helper.findEventInDataClass(title, costCurrent, percent, dateObject, repeat, tag);
							
							int recur = helper.recurStringToInt(checkedData[4]);
							
							if(indexToModify != -1) {
								user.getEvent(indexToModify).setTitle(checkedData[0]);
								user.getEvent(indexToModify).setAmount(helper.currencyToDouble(checkedData[1]));
								user.getEvent(indexToModify).setPercentage(Integer.parseInt(checkedData[2]));
								user.getEvent(indexToModify).setDate(newDateObject);
								user.getEvent(indexToModify).setRecurPeriod(recur);
								user.getEvent(indexToModify).setTag(checkedData[5]);
							}
							else {
								System.out.println("not found");
							}
							
							// if old cost is recurring calculate the total amount to take away
							if(repeat != 0 && repeat != 365 && repeat != 30) {
								double recurTotalCost = costCurrent;
								// find last day of the selected month
									int finalDay = 0;
									if(tableMonth == 1 || tableMonth == 3 || tableMonth == 5 || tableMonth == 7 || tableMonth == 8 || tableMonth == 10 || tableMonth == 12) {
										// last day is 31st
										finalDay = 31;
									}
									else if(tableMonth == 2) {
										// last day is 28th
										finalDay = 28;
									}
									else {
										// last day is 30th
										finalDay = 30;
									}
									
									// calculate the cost of recurring events over the whole month
									recurTotalCost = 0;
									for(int j = dayCurrent; j <= finalDay; j+= repeat) {
										recurTotalCost += costCurrent;
									}
								
								totalBudgeted.setText(helper.formatCurrency(helper.currencyToDouble(totalBudgeted.getText()) - recurTotalCost));
							}
							else {
								totalBudgeted.setText(helper.formatCurrency(helper.currencyToDouble(totalBudgeted.getText()) - costCurrent));
							}
							
							// if new cost is recurring calculate the total amount to take add
							int newRepeat = helper.recurStringToInt(checkedData[4]);
							if(newRepeat != 0 && newRepeat != 365 && newRepeat != 30) {
								double recurTotalCost = helper.currencyToDouble(checkedData[1]);
								// find last day of the selected month
									int finalDay = 0;
									if(tableMonth == 1 || tableMonth == 3 || tableMonth == 5 || tableMonth == 7 || tableMonth == 8 || tableMonth == 10 || tableMonth == 12) {
										// last day is 31st
										finalDay = 31;
									}
									else if(tableMonth == 2) {
										// last day is 28th
										finalDay = 28;
									}
									else {
										// last day is 30th
										finalDay = 30;
									}
									
									// calculate the cost of recurring events over the whole month
									recurTotalCost = 0;
									for(int j = dayCurrent; j <= finalDay; j+= newRepeat) {
										recurTotalCost += helper.currencyToDouble(checkedData[1]);
									}
								
								totalBudgeted.setText(helper.formatCurrency(helper.currencyToDouble(totalBudgeted.getText()) + recurTotalCost));
							}
							else {
								totalBudgeted.setText(helper.formatCurrency(helper.currencyToDouble(totalBudgeted.getText()) + helper.currencyToDouble(checkedData[1])));
							}
														
							for(int i = 0; i < 6; i++) {
								data.get(row)[i] = checkedData[i];
								model.setValueAt(checkedData[i], row, i);
							}
							
							user.getIncome(tableYear, tableMonth).setBalance(sum - helper.currencyToDouble(totalBudgeted.getText()));
							leftToBudget.setText(helper.formatCurrency(sum - helper.currencyToDouble(totalBudgeted.getText())));
							
							
							name.setText("");
							cost.setText("0");
							percentage.setText("0");
							day.setText("1");
							repeating.setSelectedIndex(0);
							category.setText("Misc");
						}
					}
				}
				else {
					// delete event
					int row = table.getSelectedRow();
					double costCurrent = helper.currencyToDouble(data.get(row)[1]);
					int repeat = helper.recurStringToInt(data.get(row)[4]);
					int dayCurrent = Integer.parseInt(data.get(row)[3]);
					
					if(row != -1) {
						// if old cost is recurring calculate the total amount to take away
						if(repeat != 0 && repeat != 365 && repeat != 30) {
							double recurTotalCost = costCurrent;
							// find last day of the selected month
								int finalDay = 0;
								if(tableMonth == 1 || tableMonth == 3 || tableMonth == 5 || tableMonth == 7 || tableMonth == 8 || tableMonth == 10 || tableMonth == 12) {
									// last day is 31st
									finalDay = 31;
								}
								else if(tableMonth == 2) {
									// last day is 28th
									finalDay = 28;
								}
								else {
									// last day is 30th
									finalDay = 30;
								}
								
								// calculate the cost of recurring events over the whole month
								recurTotalCost = 0;
								for(int j = dayCurrent; j <= finalDay; j+= repeat) {
									recurTotalCost += costCurrent;
								}
							
							totalBudgeted.setText(helper.formatCurrency(helper.currencyToDouble(totalBudgeted.getText()) - recurTotalCost));
						}
						else {
							totalBudgeted.setText(helper.formatCurrency(helper.currencyToDouble(totalBudgeted.getText()) - costCurrent));
						}
						
						user.getIncome(tableYear, tableMonth).setBalance(sum - helper.currencyToDouble(totalBudgeted.getText()));
						leftToBudget.setText(helper.formatCurrency(sum - helper.currencyToDouble(totalBudgeted.getText())));
											
						String title = data.get(row)[0];
						int percent = Integer.parseInt(data.get(row)[2]);
						String tag = data.get(row)[5];
						
						DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						String dateString = dayCurrent + "/" + tableMonth + "/" + tableYear;
						
						Date dateObject = new Date();
						try {
							dateObject = sdf.parse(dateString);
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
												
						int indexToRemove = helper.findEventInDataClass(title, costCurrent, percent, dateObject, repeat, tag);
						
						if(indexToRemove != -1) {
							user.removeEvent(indexToRemove);
						}
						
						data.remove(row);
						model.removeRow(row);
					}
				}
			}
			
			// changes the text on the button
			if(e.getSource() == eventComboBox) {
				if(eventComboBox.getSelectedIndex() == 0) {
					// add event
					submitChanges.setText("Add Row");
				}
				else if(eventComboBox.getSelectedIndex() == 1) {
					// modify event
					submitChanges.setText("Modify Selected Row");
				}
				else {
					// delete event
					submitChanges.setText("Delete Selected Row");
				}
			}
						
			if(e.getSource() == payOne) {				
				helper.updateStats(payOne, 0, 0);
			}
			else if(e.getSource() == payTwo) {
				helper.updateStats(payTwo, 0, 1);
			}
			else if(e.getSource() == payThree) {
				helper.updateStats(payThree, 0, 2);
			}
			else if(e.getSource() == payFour) {
				helper.updateStats(payFour, 0, 3);
			}
			else if(e.getSource() == tips) {
				helper.updateStats(tips, 2, 0);
			}
			else if(e.getSource() == miscOne) {				
				helper.updateStats(miscOne, 1, 0);
			}
			else if(e.getSource() == miscTwo) {
				helper.updateStats(miscTwo, 1, 1);
			}
			else if(e.getSource() == miscThree) {
				helper.updateStats(miscThree, 1, 2);
			}
			else if(e.getSource() == miscFour) {
				helper.updateStats(miscFour, 1, 3);
			}
		}
	}
	
	/*
	 * The helper class provides supporting functions for the main classes in this file. 
	 * @author brendanperry
	 */
	public class Helper {
		
		/*
		 * This function takes the word form of a recurrence period and returns the number form
		 * @params recurPeriod this is the word form
		 * @return int is the number form
		 * @author brendanperry
		 */
		public int recurStringToInt(String recurPeriod) {
			if(recurPeriod.equals("Daily")) {
				return 1;
			}
			else if(recurPeriod.equals("Weekly")) {
				return 7;
			}
			else if(recurPeriod.equals("Biweekly")) {
				return 14;
			}
			else if(recurPeriod.equals("Monthly")) {
				return 30;
			}
			else if(recurPeriod.equals("Yearly")){
				return 365;
			}
			else {
				return 0;
			}
		}
		
		/*
		 * This function returns the currency formatted for the United States
		 * @params amount - the double amount to be formatted
		 * @return the formatted currency
		 */
		public String formatCurrency(double amount) {
			return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
		}
		
		/*
		 * This function takes the integer value of a recurrence period and returns the word form
		 * @params number form of recurrence
		 * @return word form of recurrence
		 * @author brendanperry
		 */
		public String recurIntToString(int recurPeriod) {
			if(recurPeriod == 1) {
				return "Daily";
			}
			else if(recurPeriod == 7) {
				return "Weekly";
			}
			else if(recurPeriod == 14) {
				return "Biweekly";
			}
			else if(recurPeriod == 30) {
				return "Monthly";
			}
			else if(recurPeriod == 365) {
				return "Yearly";
			}
			else {
				return "None";
			}
		}
		
		/*
		 * This function loads data about from the user profile and inserts it into the table. This includes expenses and income.
		 * @params userProfile is the profile of the current user
		 * @author brendanperry
		 */
		@SuppressWarnings("unused")
		public void loadUserData(int month, int year) throws ParseException {
			clearData();
			totalBudgeted.setText("$0.00");
			leftToBudget.setText("$0.00");
			sum = 0;
			
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(Integer.toString(year) + "-" + Integer.toString(month) + "-01");
			Date prevDate;
			Date lastYearDate = format.parse(Integer.toString(year - 1) + "-" + Integer.toString(month) + "-01");
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);		
			String m = Integer.toString(month);
			String y = Integer.toString(calendar.get(Calendar.YEAR));
			String prevM;
			String prevMYear;
			String lastYearM = m;
			String lastYearY = Integer.toString(calendar.get(Calendar.YEAR) - 1);
			
			if(month == 1) {
				prevDate = format.parse(Integer.toString(year) + "-" + Integer.toString(12) + "-01");
				prevM = Integer.toString(12);
				prevMYear = Integer.toString(calendar.get(Calendar.YEAR) - 1);
			}
			else {
				prevDate = format.parse(Integer.toString(year) + "-" + Integer.toString(month - 1) + "-01");
				prevM = Integer.toString(month - 1);
				prevMYear = Integer.toString(calendar.get(Calendar.YEAR));
			}
						
			tableMonth = month;
			tableYear = calendar.get(Calendar.YEAR);
			
			String startDay = y + "-" + m + "-" + "1";
			String endDay = y + "-" + m + "-";
			String prevMStartDay = prevMYear + "-" + prevM + "-" + "1";
			String prevMEndDay = prevMYear + "-" + prevM + "-";
			String lastYearStartDay = lastYearY + "-" + lastYearM + "-" + "1";
			String lastYearEndDay = lastYearY + "-" + lastYearM + "-";
			
			if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
				// last day is 31st
				endDay += "31";
				prevMEndDay += "31";
				lastYearEndDay += "31";
			}
			else if(month == 2) {
				// last day is 28th
				endDay += "28";
				prevMEndDay += "28";
				lastYearEndDay += "28";
			}
			else {
				// last day is 30th
				endDay += "30";
				prevMEndDay += "30";
				lastYearEndDay += "30";
			}
			
			Date start = format.parse(startDay);
			Date end = format.parse(endDay);

			Event[] events = user.getEvents(start, end);
			Event[] lastMonthEvents;
			Event[] lastYearEvents = user.getEvents(format.parse(lastYearStartDay), format.parse(lastYearEndDay));
			
			boolean isNew = user.isNewMonth(tableYear, tableMonth);

			if(isNew) {
				lastMonthEvents = user.getEvents(format.parse(prevMStartDay), format.parse(prevMEndDay));
				
				// load recurring events from last month
				for(int i = 0; i < lastMonthEvents.length; i++) {
					if(lastMonthEvents[i].getRecurPeriod() != 0 && lastMonthEvents[i].getRecurPeriod() != 365) {
						String title = lastMonthEvents[i].getTitle();
						Double cost = lastMonthEvents[i].getAmount();
						
						String percent = Integer.toString(lastMonthEvents[i].getPercentage());
						String category = lastMonthEvents[i].getTag();	
						
						Calendar cal = Calendar.getInstance();
						cal.setTime(lastMonthEvents[i].getDate());			
						int dayInt = cal.get(Calendar.DAY_OF_MONTH);
						String day = Integer.toString(dayInt);
						
						int repeating = lastMonthEvents[i].getRecurPeriod();
						
						// find last day of the selected month
						int finalDay = 0;
						if(tableMonth == 1 || tableMonth == 3 || tableMonth == 5 || tableMonth == 7 || tableMonth == 8 || tableMonth == 10 || tableMonth == 12) {
							// last day is 31st
							finalDay = 31;
						}
						else if(tableMonth == 2) {
							// last day is 28th
							finalDay = 28;
						}
						else {
							// last day is 30th
							finalDay = 30;
						}
						
						// calculate the cost of recurring events over the whole month
						double recurTotalCost = cost;
						if(repeating != 30) {
							recurTotalCost = 0;
							for(int j = dayInt; j <= finalDay; j+= repeating) {
								recurTotalCost += cost;
							}
						}
					
						String[] newData = {title, formatCurrency(cost), percent, day, recurIntToString(repeating), category};
						totalBudgeted.setText(formatCurrency(currencyToDouble(totalBudgeted.getText()) + recurTotalCost));
						
						data.add(newData);
						model.addRow(newData);
						saveUserData(newData);
					}
				}
			}
			
			// load income
			
			try {						
				double pay1 = user.getIncome(tableYear, tableMonth).getWeek(0);
				double pay2 = user.getIncome(tableYear, tableMonth).getWeek(1);
				double pay3 = user.getIncome(tableYear, tableMonth).getWeek(2);
				double pay4 = user.getIncome(tableYear, tableMonth).getWeek(3);
				double tempTips = user.getIncome(tableYear, tableMonth).getTips();
				double tempMisc1 = user.getIncome(tableYear, tableMonth).getMisc(0);
				double tempMisc2 = user.getIncome(tableYear, tableMonth).getMisc(1);
				double tempMisc3 = user.getIncome(tableYear, tableMonth).getMisc(2);
				double tempMisc4 = user.getIncome(tableYear, tableMonth).getMisc(3);
				 
				payOne.setText(formatCurrency(pay1));
				payTwo.setText(formatCurrency(pay2));
				payThree.setText(formatCurrency(pay3));
				payFour.setText(formatCurrency(pay4));
				tips.setText(formatCurrency(tempTips));
				miscOne.setText(formatCurrency(tempMisc1));
				miscTwo.setText(formatCurrency(tempMisc2));
				miscThree.setText(formatCurrency(tempMisc3));
				miscFour.setText(formatCurrency(tempMisc4));
				
				sum = pay1 + pay2 + pay3 + pay4 + tempTips + tempMisc1 + tempMisc2 + tempMisc3 + tempMisc4;
				totalIncome.setText(formatCurrency(sum));
			}
			catch(Exception e) {
				System.out.println(e);
			}
			
			// load yearly events from a year ago
			if(isNew) {
				for(int i = 0; i < lastYearEvents.length; i++) {
					if(lastYearEvents[i].getRecurPeriod() == 365) {
						String title = lastYearEvents[i].getTitle();
						Double cost = lastYearEvents[i].getAmount();
						
						String percent = Integer.toString(lastYearEvents[i].getPercentage());
						String category = lastYearEvents[i].getTag();	
						
						Calendar cal = Calendar.getInstance();
						cal.setTime(lastYearEvents[i].getDate());			
						int dayInt = cal.get(Calendar.DAY_OF_MONTH);
						String day = Integer.toString(dayInt);
											
						String[] newData = {title, formatCurrency(cost), percent, day, "Yearly", category};
						
						totalBudgeted.setText(formatCurrency(currencyToDouble(totalBudgeted.getText()) + cost));
						
						data.add(newData);
						model.addRow(newData);
						saveUserData(newData);
					}
				}
			}

			for(int i = 0; i < events.length; i++) {
				String title = events[i].getTitle();
				Double cost = events[i].getAmount();
				
				String percent = Integer.toString(events[i].getPercentage());
				String category = events[i].getTag();	
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(events[i].getDate());			
				int dayInt = cal.get(Calendar.DAY_OF_MONTH);
				String day = Integer.toString(dayInt);
				
				int repeating = events[i].getRecurPeriod();
				
				if(repeating == 0 || repeating == 30) {
					String[] newData = {title, formatCurrency(cost), percent, day, "None", category};
					
					totalBudgeted.setText(formatCurrency(currencyToDouble(totalBudgeted.getText()) + cost));
					
					data.add(newData);
					model.addRow(newData);
				}
				else {
					// find last day of the selected month
					int finalDay = 0;
					if(tableMonth == 1 || tableMonth == 3 || tableMonth == 5 || tableMonth == 7 || tableMonth == 8 || tableMonth == 10 || tableMonth == 12) {
						// last day is 31st
						finalDay = 31;
					}
					else if(tableMonth == 2) {
						// last day is 28th
						finalDay = 28;
					}
					else {
						// last day is 30th
						finalDay = 30;
					}
					
					// calculate the cost of recurring events over the whole month
					double recurTotalCost = 0;
					for(int j = dayInt; j <= finalDay; j+= repeating) {
						recurTotalCost += cost;
					}
				
					String[] newData = {title, formatCurrency(cost), percent, day, recurIntToString(repeating), category};
					
					totalBudgeted.setText(formatCurrency(currencyToDouble(totalBudgeted.getText()) + recurTotalCost));
					
					data.add(newData);
					model.addRow(newData);
				}
			}
			
			user.getIncome(tableYear, tableMonth).setBalance(sum - currencyToDouble(totalBudgeted.getText()));
			leftToBudget.setText(formatCurrency(sum - currencyToDouble(totalBudgeted.getText())));
		}
		
		/*
		 * This functions removes all data that is stored and shown in the table.
		 * @author brendanperry
		 */
		private void clearData() {
			int size = data.size();
			
			for(int i = 0; i < size; i++) {
				data.remove(0);
				model.removeRow(0);
			}
		}
		
		/*
		 * This function saves user data that has been added to the table.
		 * @params it takes info which is the list of all event information
		 * @author brendanperry
		 */
		private void saveUserData(String[] info) throws ParseException {			
			DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String dateString = info[3] + "/" + tableMonth + "/" + tableYear;
						
			Date dateObject = sdf.parse(dateString);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateObject);
						
			Event event;
			
			if(info[4].equals("None")) {
				// create non repeating event
				event = new Event(info[0], currencyToDouble(info[1]), Integer.parseInt(info[2]), dateObject, 0, info[5]);
			}
			else {
				// create repeating event
				int recurPeriod = 0;
				
				if(info[4].equals("Weekly")) {
					recurPeriod = 7;
				}
				else if(info[4].equals("Biweekly")) {
					recurPeriod = 14;
				}
				else if(info[4].equals("Monthly")) {
					recurPeriod = 30;
				}
				else if(info[4].equals("Daily")) {
					recurPeriod = 1;
				}
				else {
					recurPeriod = 365;
				}
				
				event = new Event(info[0], currencyToDouble(info[1]), Integer.parseInt(info[2]), dateObject, recurPeriod, info[5]);
			}
			
			user.addEvent(event);
		}

		/*
		 * updateStats is used to update the the total income and left to budget text fields.
		 * It is called when the user adds new pay information.
		 * @params textField is the text field that the data should be updated from, num is the 
		 * paycheck number (1 - 5) with 5 being tips. 6 and up is misc pay
		 * @author brendanperry
		 */
		public void updateStats(JTextField textField, int type, int week) {
			String text = textField.getText();
			Double temp = 0.00;
								
			try {
				if(!text.isEmpty()) {
					if(text.toCharArray()[0] == '$') {
						temp = Double.parseDouble(text.substring(1, text.length()));
					}
					else {
						temp = Double.parseDouble(text);
					}
					
					if(temp < 0) {
						temp = 0.0;
					}
					
					textField.setText(formatCurrency(temp));
				}			
			}
			catch (Exception e) {
				totalIncome.setText("NaN");
				leftToBudget.setText("NaN");
			}
			
			if(type == 0) {
				user.getIncome(tableYear, tableMonth).setWeek(temp, week);
			}
			else if(type == 1) {
				user.getIncome(tableYear, tableMonth).setMisc(temp, week);
			}
			else {
				user.getIncome(tableYear, tableMonth).setTips(temp);
			}
			
			sum = currencyToDouble(payOne.getText()) + currencyToDouble(payTwo.getText()) + currencyToDouble(payThree.getText()) + currencyToDouble(payFour.getText())
			+ currencyToDouble(tips.getText()) + currencyToDouble(miscOne.getText()) + currencyToDouble(miscTwo.getText()) + currencyToDouble(miscThree.getText()) + 
			currencyToDouble(miscFour.getText());
			
			totalIncome.setText(formatCurrency(sum));
			updatePercentages();
			
			user.getIncome(tableYear, tableMonth).setBalance(sum - currencyToDouble(totalBudgeted.getText()));
			leftToBudget.setText(formatCurrency(sum - currencyToDouble(totalBudgeted.getText())));
		}
		
		/*
		 * This function will find an event from the UserProfile that matches the information given
		 * @params all information for a given event
		 * @return returns the index of the event in the data class
		 * @author brendanperry
		 */
		private int findEventInDataClass(String title, double cost, int percent, Date day, int recurPeriod, String tag) {
			int index = -1;
			int length = user.getNumberOfEvents();
			
			for(int i = 0; i < length; i++) {
				Event event = user.getEvent(i);		
				
				if(event.getTitle().equals(title)) {
					// title matches 				
					if(event.getAmount() == cost) {
						// cost matches			
						if(event.getPercentage() == percent) {
							// percent matches		
							if(event.getDate().equals(day)) {
								// date matches			
								if(event.getRecurPeriod() == recurPeriod) {
									// repeat matches		
									if(event.getTag().equals(tag)) {
										// tag matches	
										index = i;
										break;
									}
								}
							}
						}
					}
				}
			}
			
			return index;
		}
		
		/*
		 * This function is used to update the cost of items that depend on a certain percentage
		 * of the users income. It will be called whenever the total income has changed.
		 * @author brendanperry
		 */
		public void updatePercentages() {
			for(int i = 0; i < data.size(); i++) {
				if(Integer.parseInt(data.get(i)[2]) > 0) {
					double oldValue = currencyToDouble(data.get(i)[1]);
					double newValue = currencyToDouble(data.get(i)[2]) / 100.0 * currencyToDouble(totalIncome.getText());
					
					totalBudgeted.setText(formatCurrency(currencyToDouble(totalBudgeted.getText()) - oldValue));
					totalBudgeted.setText(formatCurrency(currencyToDouble(totalBudgeted.getText()) + newValue));
					data.get(i)[1] = formatCurrency(newValue);
					model.setValueAt(data.get(i)[1], i, 1);
				}
			}
		}
		
		/*
		 * This function is used to convert text that is formatted using NumberFormat to a double value.
		 * @params numberText is the formatted text to be converted
		 * @return returns a decimal value with no '$' or ','
		 * @author brendanperry
		 */
		public double currencyToDouble(String numberText) {
			String noFormatting = numberText.replaceAll("[$,]", "");
			try {
				return Double.parseDouble(noFormatting);
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Invalid Data");
				return 0;
			}
		}
		
		/*
		 * This method checks the data to see if its valid and properly formats it for the table
		 * @author brendanperry
		 * @params String[] data is an array of all the event data
		 * @return returns the newly formatted string array
		 */
		private String[] checkDataValidity(String[] data) {
			String[] returnData = data;
			
			for(int i = 0; i < 5; i++) {
				if(data[i].isEmpty()) {
					JOptionPane.showMessageDialog(null, "Missing Data");
					return null;
				}
			}
			
			try {
				int percent = Integer.parseInt(data[2]);
				if(percent < 0 || percent > 100) {
					JOptionPane.showMessageDialog(null, "Invalid Percentage");
					return null;
				}
				else {
					double cost = -1;
					
					if(percent > 0) {
						cost = (percent / 100.0) * currencyToDouble(totalIncome.getText());
						returnData[1] = formatCurrency(cost);
					}
					else {
						try {
							cost = currencyToDouble(data[1]);
							
							if(cost < 0) {
								JOptionPane.showMessageDialog(null, "Invalid Cost");
								return null;
							}
							
							returnData[1] = formatCurrency(cost);
						}
						catch (Exception ex) {
							JOptionPane.showMessageDialog(null, "Invalid Cost");
							return null;
						}
					}
				}
			}
			catch(Exception per) {
				JOptionPane.showMessageDialog(null, "Invalid Percentage");
				return null;
			}
			
			int day = -1;
			
			try {
				day = Integer.parseInt(data[3]);
				
				int endDay = 0;
				
				if(tableMonth == 1 || tableMonth == 3 || tableMonth == 5 || tableMonth == 7 || tableMonth == 8 || tableMonth == 10 || tableMonth == 12) {
					// last day is 31st
					endDay = 31;
				}
				else if(tableMonth == 2) {
					// last day is 28th
					endDay = 28;
				}
				else {
					// last day is 30th
					endDay = 30;
				}
				
				if(day < 0 || day > endDay) {
					JOptionPane.showMessageDialog(null, "Invalid Day");
					return null;
				}
			}
			catch (Exception exc) {
				JOptionPane.showMessageDialog(null, "Invalid Day");
				return null;
			}
			
			return returnData;
		}
	}
}