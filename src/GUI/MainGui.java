package GUI;

import IO.DataBase;
import Support.DataNameUtils;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class MainGui {

    JPanel jMainPanel;
    JPanel jMainPanelUser;
    JPanel jMainPanelAdmin;
    JPanel jMainPanelRoot;
    private JFrame frame;
    private JTable jTable;//当前界面的Table
    private DefaultTableModel tableModel;
    private  int userId = 0;
    private String userName;
    private String au;
    private ResultSet resultSet;
    private int customerid;
    private JTextField textDialogName;
    private JPasswordField passwordText;
    private JTextField userText;
    private JComboBox adminText;
    private static int authority;
    private DataBase dataBase;
    private static int SCREEN_WIDTH;
    private static int SCREEN_HEIGHT;

    private String PANEL_MODE = "";//users, stuff, car ....
    private int delete_row_id = -1;
    private Vector<String> columns;
    private JScrollPane scrollPane;
    private JPanel contentPane;


    public static void main(String[] args) {

        if(UIManager.getLookAndFeel().isSupportedLookAndFeel()){
            final String platform = UIManager.getSystemLookAndFeelClassName();
            // If the current Look & Feel does not match the platform Look & Feel,
            // change it so it does.
            if (!UIManager.getLookAndFeel().getName().equals(platform)) {
                try {
                    UIManager.setLookAndFeel(platform);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        setFontForBeautyEye();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH = dim.width;
        SCREEN_HEIGHT = dim.height;

        new MainGui().RunBabyRun();

    }

    /**
     * 修复字体发虚
     */
    private static void setFontForBeautyEye() {
        String[] DEFAULT_FONT = new String[]{
                "Table.font"
                , "TableHeader.font"
                , "CheckBox.font"
                , "Tree.font"
                , "Viewport.font"
                , "ProgressBar.font"
                , "RadioButtonMenuItem.font"
                , "ToolBar.font"
                , "ColorChooser.font"
                , "ToggleButton.font"
                , "Panel.font"
                , "TextArea.font"
                , "Menu.font"
                , "TableHeader.font"
                , "OptionPane.font"
                , "MenuBar.font"
                , "Button.font"
                , "Label.font"
                , "PasswordField.font"
                , "ScrollPane.font"
                , "MenuItem.font"
                , "ToolTip.font"
                , "List.font"
                , "EditorPane.font"
                , "Table.font"
                , "TabbedPane.font"
                , "RadioButton.font"
                , "CheckBoxMenuItem.font"
                , "TextPane.font"
                , "PopupMenu.font"
                , "TitledBorder.font"
                , "ComboBox.font"
        };

        for (String aDEFAULT_FONT : DEFAULT_FONT) {
            UIManager.put(aDEFAULT_FONT, new Font("微软雅黑", Font.PLAIN, 12));
        }
    }

    private void RunBabyRun() {
        dataBase = DataBase.getInstance();
        frame = new JFrame();//顺便初始化一下父容器

        if (dataBase.initConnect()) {

            frame.setIconImage(Toolkit.getDefaultToolkit().getImage("res/car.png"));
            frame.setTitle("\u767B\u5F55");
            frame.setFont(new Font("黑体", Font.PLAIN, 14));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBounds(100, 100, 450, 300);

            frame.setLocationRelativeTo(null);
            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
            frame.setContentPane(contentPane);
            contentPane.setLayout(null);

            JLabel lblNewLabel = new JLabel("New label");
            lblNewLabel.setIcon(new ImageIcon("res/car.png"));
            lblNewLabel.setBounds(45, 49, 128, 128);
            contentPane.add(lblNewLabel);

            JLabel lblNewLabel_1 = new JLabel("\u6C7D\u8F66\u79DF\u8D41\u7CFB\u7EDF");
            lblNewLabel_1.setFont(new Font("黑体", Font.PLAIN, 14));
            lblNewLabel_1.setBounds(65, 187, 100, 15);
            contentPane.add(lblNewLabel_1);

            JLabel lblNewLabel_2 = new JLabel("\u7528\u6237\u540D\uFF1A");
            lblNewLabel_2.setIcon(new ImageIcon("res/user.png"));
            lblNewLabel_2.setFont(new Font("黑体", Font.PLAIN, 15));
            lblNewLabel_2.setBounds(200, 50, 120, 25);
            contentPane.add(lblNewLabel_2);

            userText = new JTextField();
            userText.setForeground(Color.DARK_GRAY);
            userText.setFont(new Font("黑体", Font.BOLD, 15));
            userText.setBounds(287, 50, 100, 25);
            contentPane.add(userText);
            userText.setColumns(10);

            JLabel lblNewLabel_3 = new JLabel(" \u5BC6\u7801\uFF1A");
            lblNewLabel_3.setIcon(new ImageIcon("res/passwd.png"));
            lblNewLabel_3.setFont(new Font("黑体", Font.PLAIN, 15));
            lblNewLabel_3.setBounds(200, 94, 90, 25);
            contentPane.add(lblNewLabel_3);

            passwordText = new JPasswordField();
            passwordText.setBounds(287, 94, 100, 25);
            contentPane.add(passwordText);

            JButton btnLogin = new JButton("登录");

            btnLogin.setFont(new Font("黑体", Font.PLAIN, 14));
            btnLogin.setBounds(200, 194, 70, 23);
            contentPane.add(btnLogin);

            JButton btnLogup = new JButton("注册");
            btnLogup.setFont(new Font("黑体", Font.PLAIN, 14));
            btnLogup.setBounds(317, 194, 70, 23);
            contentPane.add(btnLogup);

            JLabel lblNewLabel_4 = new JLabel("\u6743\u9650\uFF1A");
            lblNewLabel_4.setIcon(new ImageIcon("res/admin.png"));
            lblNewLabel_4.setFont(new Font("黑体", Font.PLAIN, 15));
            lblNewLabel_4.setBounds(200, 144, 100, 25);
            contentPane.add(lblNewLabel_4);

            adminText = new JComboBox();
            adminText.setFont(new Font("黑体", Font.BOLD, 13));
            adminText.setModel(new DefaultComboBoxModel(new String[] {"\u7528\u6237", "\u7BA1\u7406\u5458", "\u8D85\u7EA7\u7BA1\u7406\u5458"}));
            adminText.setBounds(287, 144, 100, 25);
            contentPane.add(adminText);
            btnLogin.addActionListener(otherListener);
            btnLogup.addActionListener(otherListener);
//            initMainFrame();
//                 frame.setContentPane(jMainPanel);
            frame.setVisible(true);


        } else {
            JOptionPane.showMessageDialog(frame, "数据库连接失败");
        }

    }

    /**
     * 菜单栏中“其他”的监听器
     */
    private ActionListener otherListener = (ActionEvent e) -> {
        String strClick = e.getActionCommand();
        System.out.println(strClick);
        String nameInput = userText.getText();
        String pswInput =new String( passwordText.getPassword());
        au =  adminText.getSelectedItem().toString();

        if(au.equals("用户"))
            authority = 0;
        else if(au.equals("管理员"))
            authority = 1;
        else authority = 2;
        switch (strClick) {
            case "注册":
                System.out.println("注册\n");
                String superCode;
                String custid = null;
                if(authority == 0){
                    custid = (new JOptionPane()).showInputDialog("请输入你的客户id");
                }
                if(authority == 1){
                    superCode = (new JOptionPane()).showInputDialog("请输入你的授权码");
                    if(!superCode.equals("admin")) {
                        noticeMsg("授权码错误");
                        break;
                    }
                } else if(authority == 2){
                    superCode = (new JOptionPane()).showInputDialog("请输入你的授权码");
                    if(!superCode.equals("root")) {
                        noticeMsg("授权码错误");
                        break;
                    }
                }
                try {int result;
                    result = dataBase.registerUser(nameInput, pswInput,authority,custid);
                    if(result < 0) noticeMsg("用户已存在");
                    else noticeMsg("注册成功，即将为你登录");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            case "登录":
                //check and set authority
                 userName = nameInput;
                try {
                   customerid = dataBase.checkUser(nameInput, pswInput,authority);
                    if (customerid != -1) {
                       frame.setVisible(false);
                        System.out.println("name:" + nameInput + "\npsw:" + pswInput + "\nauthor" + au +" " + authority + " " + customerid);
                        if(authority== 0)
                        initUserMainFrame();
                        if(authority == 1)
                            initAdminMainFrame();
                        if(authority == 2)
                            initRootMainFrame();
                    } else {
                        noticeMsg("用户名或密码错误");
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    noticeMsg("数据库连接失败");
                }
                break;
        }
    };

    /**
     * 初始化主框架
     */
    private void initMainFrame() {
        jMainPanel.removeAll();
        try {
            jMainPanel = new BackgrouPanel("res/background.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }



        jMainPanel.setLayout(new BorderLayout());
        jMainPanel.updateUI();
    }

    /**
     * 初始用户化主框架
     */
    private void initUserMainFrame() {
        try {
            jMainPanelUser = new BackgrouPanel("res/background3.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setContentPane(jMainPanelUser);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        String title = String.format("欢迎%s : %s",au,userName);
        frame.setTitle(title);

        frame.setSize(900, 600);
        setCenter(frame);
        frame.setVisible(true);

        JTextFiledOpen field = new JTextFiledOpen();
        field.setFont(new Font("黑体", Font.BOLD, 30));
        field.setText("\n汽车租赁信息系统 - 用户端");
        field.setForeground(Color.white);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setBorder(null);
        field.setEditable(false);

        frame.getContentPane().setLayout(null);
        jMainPanelUser.add(field);
        field.setBounds(170,35,500,40);

        JLabel username = createLabel("用户名：" + userName,150,150);

        jMainPanelUser.add(username);
        String name = new String();
        String member = new String(); ;
        if(customerid != 0){
        try {
            resultSet = dataBase.getUserDetail(customerid);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            if(resultSet.next()){
            name = resultSet.getString("name");
            member = resultSet.getString("member");}

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }}

        JLabel cust = createLabel("顾客id：" + customerid,150,200);
        JLabel cName = createLabel("姓名：" + name,150,250);
        JLabel cMem = createLabel("会员状态：" + member,150,300);
//        JButton changeButton = createButton("变更信息",450,200);
        JButton carButton = createButton("查询车辆",450,170);
        JButton infoButton = createButton("使用记录",450,220);
        JButton pswButton = createButton("更改密码",450,270);
        JButton logoutButton = createButton("切换用户",450,320);
        carButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.blue));
        infoButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
        pswButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        logoutButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
//        jMainPanelUser.add(changeButton);
        jMainPanelUser.add(carButton);
        jMainPanelUser.add(infoButton);
        jMainPanelUser.add(pswButton);
        jMainPanelUser.add(logoutButton);
        carButton.addActionListener(userListener);
        infoButton.addActionListener(userListener);
        pswButton.addActionListener(userListener);
        logoutButton.addActionListener(userListener);
        if(customerid != 0 ){
            jMainPanelUser.add(cust);
            jMainPanelUser.add(cName);
            jMainPanelUser.add(cMem);
        }
        jMainPanelUser.add(username);
        jMainPanel.updateUI();
    }


    /**
     * 初始管理员化主框架
     */
    private void initAdminMainFrame() {
        try {
            jMainPanelAdmin = new BackgrouPanel("res/background3.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setContentPane(jMainPanelAdmin);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        String title = String.format("欢迎%s : %s",au,userName);
        frame.setTitle(title);

        frame.setSize(900, 600);
        setCenter(frame);
        frame.setVisible(true);

        JTextFiledOpen field = new JTextFiledOpen();
        field.setFont(new Font("黑体", Font.BOLD, 30));
        field.setText("\n汽车租赁信息系统 - 管理员端");
        field.setForeground(Color.white);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setBorder(null);
        field.setEditable(false);

        frame.getContentPane().setLayout(null);
        jMainPanelAdmin.add(field);
        field.setBounds(170,35,500,40);


        JButton carButton = createButton("查询车辆",300,200);
        JButton infoButton = createButton("使用记录",300,250);
        JButton customerButton = createButton("所有顾客",300,300);
        JButton userButton = createButton("所有用户",500,200);
        JButton pswButton = createButton("更改密码",500,250);
        JButton logoutButton = createButton("切换用户",500,300);
        carButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.blue));
        infoButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
        pswButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        logoutButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
        customerButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        userButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
        jMainPanelAdmin.add(logoutButton);
        jMainPanelAdmin.add(carButton);
        jMainPanelAdmin.add(infoButton);
        jMainPanelAdmin.add(pswButton);
        jMainPanelAdmin.add(customerButton);
        jMainPanelAdmin.add(userButton);
        logoutButton.addActionListener(userListener);
        carButton.addActionListener(userListener);
        infoButton.addActionListener(userListener);
        pswButton.addActionListener(userListener);
        customerButton.addActionListener(userListener);
        userButton.addActionListener(userListener);
        jMainPanelAdmin.updateUI();
    }

    /**
     * 初始超级管理员主框架
     */
    private void initRootMainFrame() {
        try {
            jMainPanelRoot = new BackgrouPanel("res/background3.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setContentPane(jMainPanelRoot);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        String title = String.format("欢迎%s : %s",au,userName);
        frame.setTitle(title);

        frame.setSize(900, 600);
        setCenter(frame);
        frame.setVisible(true);

        JTextFiledOpen field = new JTextFiledOpen();
        field.setFont(new Font("黑体", Font.BOLD, 30));
        field.setText("\n汽车租赁信息系统 - 超级管理员端");
        field.setForeground(Color.white);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setBorder(null);
        field.setEditable(false);

        frame.getContentPane().setLayout(null);
        jMainPanelRoot.add(field);
        field.setBounds(170,35,500,40);


        JButton carButton = createButton("查询车辆",200,200);
        JButton infoButton = createButton("使用记录",200,250);
        JButton customerButton = createButton("所有顾客",200,300);
        JButton stuffButton = createButton("所有员工",200,350);
        JButton userButton = createButton("所有用户",400,200);
        JButton pswButton = createButton("更改密码",400,250);
        JButton logoutButton = createButton("切换用户",400,300);
        JButton creditButton = createButton("信誉度统计",400,350);
        JButton totalButton = createButton("统计数据",600,200);
        carButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.blue));
        infoButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
        pswButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        logoutButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
        customerButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        userButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
        stuffButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        creditButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        totalButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.blue));
        jMainPanelRoot.add(logoutButton);
        jMainPanelRoot.add(carButton);
        jMainPanelRoot.add(infoButton);
        jMainPanelRoot.add(pswButton);
        jMainPanelRoot.add(customerButton);
        jMainPanelRoot.add(userButton);
        jMainPanelRoot.add(stuffButton);
        jMainPanelRoot.add(creditButton);
        jMainPanelRoot.add(totalButton);
        logoutButton.addActionListener(userListener);
        carButton.addActionListener(userListener);
        infoButton.addActionListener(userListener);
        pswButton.addActionListener(userListener);
        customerButton.addActionListener(userListener);
        userButton.addActionListener(userListener);
        stuffButton.addActionListener(userListener);
        creditButton.addActionListener(e -> showCredit());
        totalButton.addActionListener(e -> showChart());
        jMainPanelRoot.updateUI();
    }

    private ActionListener userListener = (ActionEvent e) -> {
        String strClick = e.getActionCommand();
        System.out.println(strClick);
        Vector<Vector<String>> vectors = null;//数据vertor
        switch (strClick) {
            case "查询车辆":
                initMainFrame();
                PANEL_MODE = "车辆";
                columns = new Vector<>(Arrays.asList(DataNameUtils.carColumns));
                try {
                    vectors = DataBase.getInstance().getCarLists();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                break;
            case "使用记录":
                initMainFrame();
                PANEL_MODE = "事件";
                columns = new Vector<>(Arrays.asList(DataNameUtils.infoColumns));
                try {
                    vectors = DataBase.getInstance().getInfoLists(authority, userName);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            case "所有顾客":
                initMainFrame();
                PANEL_MODE = "顾客";
                columns = new Vector<>(Arrays.asList(DataNameUtils.customerColumns));
                try {
                    vectors = DataBase.getInstance().getCustomerLists();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println(vectors);
                break;
            case "所有用户":
                initMainFrame();
                PANEL_MODE = "用户";
                columns = new Vector<>(Arrays.asList(DataNameUtils.usersColumns));
                try {
                    vectors = DataBase.getInstance().getUserLists(authority,userName);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println(vectors);
                break;
            case "所有员工":
                initMainFrame();
                PANEL_MODE = "员工";
                columns = new Vector<>(Arrays.asList(DataNameUtils.stuffColumns));
                try {
                    vectors = DataBase.getInstance().getStuffLists();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println(vectors);
                break;
            case "更改密码":
                String psw = (new JOptionPane()).showInputDialog("请输入新密码");
                try {
                    dataBase.updatePsw(userName,psw);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                showLogin();
                break;
            case "切换用户":
                showLogin();
                break;
        }
        if (vectors != null && columns != null) {
            jMainPanel.setLayout(new BorderLayout());
            setTablePanel(vectors, columns);
            frame.getContentPane().removeAll();
            //重新渲染整个界面
            JPanelOpen jPanelSearch = new JPanelOpen();
            setSearchPanel(jPanelSearch);
            jMainPanel.add(jPanelSearch, BorderLayout.CENTER);
            jMainPanel.add(scrollPane, BorderLayout.SOUTH);
            frame.setContentPane(jMainPanel);
            frame.pack();
            jMainPanel.updateUI();

        }
        else if(!strClick.equals("切换用户")) noticeMsg("不存在相关数据");
    };
    public void showLogin(){
        frame.getContentPane().removeAll();
        frame.setContentPane(contentPane);
        frame.setSize(450,300);
    }

public JLabel createLabel(String s,int x,int y){
    JLabel label= new JLabel(s);
    label.setForeground(Color.white);
    label.setBounds(x+30,y+30,200,30);
    label.setFont(new Font("黑体",Font.PLAIN,20));
    return label;
}

public JButton createButton(String s,int x,int y){
        JButton button = new JButton(s);
        button.setBorder(new LineBorder(Color.white));
        button.setBackground(Color.white);
        button.setForeground(Color.black);
        button.setBounds(x,y,150,30);
        button.setFont(new Font("黑体",Font.BOLD,17));
        return button;
}



    /**
     * 展示图表
     */
    private void showChart() {
        JDialog chartDialog = new JDialog(frame);
        JPanel contChartPane = new JPanel(new BorderLayout());
        chartDialog.setContentPane(contChartPane);
        chartDialog.setTitle("统计图表");

        JPanel argPanel = new JPanel(new FlowLayout());

        JLabelOpen jLabelF = new JLabelOpen("纵轴：");
        JLabelOpen jLabelM = new JLabelOpen("横轴：");

        JComboBox<String> jComboxFunc = new JComboBox<>(new String[]{"借车", "还车", "损坏维修", "罚款"});
        JComboBox<String> jComboxMode = new JComboBox<>(new String[]{"年", "月", "日"});


        JPanelOpen jPOF = new JPanelOpen();
        JPanelOpen jPOM = new JPanelOpen();

        jPOF.add(jLabelF);
        jPOF.add(jComboxFunc);
        jPOM.add(jLabelM);
        jPOM.add(jComboxMode);

        JButton jConfirmBut = new JButton("确定");
        jConfirmBut.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));

        argPanel.add(jPOF);
        argPanel.add(jPOM);
        argPanel.add(jConfirmBut);


        JPanelOpen chartPanel = new JPanelOpen();

        jConfirmBut.addActionListener(e -> {
            drawChart(((String) jComboxMode.getSelectedItem()),
                    ((String) jComboxFunc.getSelectedItem()),
                    chartPanel);
            chartDialog.pack();
        });


        contChartPane.add(argPanel, BorderLayout.CENTER);
        contChartPane.add(chartPanel, BorderLayout.SOUTH);

        chartDialog.setSize(new Dimension(600, 500));
        chartDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setCenter(chartDialog);
    }

    //绘制图表界面
    private void drawChart(String mode, String func, JPanel panel) {
        panel.removeAll();
        String horizon;
        if (func.equals("流水")) {
            horizon = "元";
        } else {
            horizon = "次数";
        }
        JFreeChart chart = ChartFactory.createBarChart3D(func, mode, horizon, getChartData(func, mode), PlotOrientation.VERTICAL, false, false, false);

        //设置字体
        CategoryPlot plot = chart.getCategoryPlot();//获取图表区域对象
        CategoryAxis domainAxis = plot.getDomainAxis();         //水平底部列表
        domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 14));         //水平底部标题
        domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));  //垂直标题
        ValueAxis rangeAxis = plot.getRangeAxis();//获取柱状
        rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
        chart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));//设置标题字体
        ChartPanel chartP = new ChartPanel(chart);
        chartP.setOpaque(false);

        panel.add(chartP);

    }

    //为图表赋值
    private CategoryDataset getChartData(String func, String mode) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            ArrayList<ArrayList<String>> dataLists = DataBase.getInstance().getAllChartData(mode, func);
            for (ArrayList<String> list : dataLists) {
                int length = list.size();
                double value = Double.parseDouble(list.get(length - 1));
                String colunmKey;
                if (length == 3) {
                    colunmKey = list.get(0) + list.get(1);
                } else {
                    colunmKey = list.get(0);
                }
                dataset.addValue(value, "gray", colunmKey);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            noticeMsg("数据库获取数据出错");
        }


        return dataset;
    }

    /**
     * 窗口放置桌面中央
     *
     * @param c component waited to be reset
     */
    private void setCenter(Component c) {
//        ((Window) c).pack();
        c.setLocation((SCREEN_WIDTH - c.getWidth()) / 2, (SCREEN_HEIGHT - c.getHeight()) / 2);
        c.setVisible(true);
    }

    private void noticeMsg(String in) {//make code elegant
        JOptionPane.showMessageDialog(frame, in);
    }


    /**
     * 根据不同的表，绘制不同的成对Label与TextField或Combobox，内部通过获取搜索栏的新约束条件获得新的结果并重绘表格Panel
     * @param jPanelSearch Search components' parent components
     */
    private void setSearchPanel(JPanelOpen jPanelSearch) {
        jPanelSearch.setLayout(new FlowLayout());
        if (PANEL_MODE.equals("车辆")) {
            setBlankInSearchPanel(jPanelSearch, DataNameUtils.carColumns);
        } else if (PANEL_MODE.equals("顾客")) {
            setBlankInSearchPanel(jPanelSearch, DataNameUtils.customerColumns);
        }
        if (PANEL_MODE.equals("用户")) {
            setBlankInSearchPanel(jPanelSearch, DataNameUtils.usersColumns);
        }
        if (PANEL_MODE.equals("事件")) {
            setBlankInSearchPanel(jPanelSearch, DataNameUtils.infoColumns);
        }
        if (PANEL_MODE.equals("员工")) {
            setBlankInSearchPanel(jPanelSearch, DataNameUtils.stuffColumns);
        }
        JButton jButton = new JButton("搜索");
        JButton button = new JButton("返回主界面");
        jButton.addActionListener(e -> {
            HashMap<String, String> dataMap = getDataMap(jPanelSearch);
            if (checkSearchData(dataMap)) {
                try {
                    //在这里利用搜索栏中新的约束条件获得新的获取结果
                    Vector<Vector<String>> vectors = null;
                    switch (PANEL_MODE) {
                        case "车辆":
                            vectors = DataBase.getInstance().getCarLists(dataMap);
                            break;
                        case "顾客":
                            vectors = DataBase.getInstance().getCustomerLists(dataMap);
                            break;
                        case "用户":
                            vectors = DataBase.getInstance().getUserLists(dataMap, authority, userName);
                            break;
                        case "员工":
                            vectors = DataBase.getInstance().getStuffLists(dataMap);
                            break;
                        case "事件":
                            vectors = DataBase.getInstance().getInfoLists(dataMap, authority, userName);
                            break;
                    }

                    setTablePanel(vectors, columns);
                    jMainPanel.add(scrollPane, BorderLayout.SOUTH);
                    jMainPanel.updateUI();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    noticeMsg("搜索数据不合法或数据库发生错误");
                }
            }
        });
        button.addActionListener(e -> {
            frame.getContentPane().removeAll();
            System.out.println(authority);
            if(authority == 0) {initUserMainFrame();
                frame.setContentPane(jMainPanelUser);}
            else if(authority == 1)
            {initAdminMainFrame();
                frame.setContentPane(jMainPanelAdmin);}
            else if(authority == 2){
                initRootMainFrame();
                frame.setContentPane(jMainPanelRoot);
            }
            frame.setSize(900,600);
            frame.setVisible(true);
        });
        jButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.blue));
        button.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        jPanelSearch.add(jButton);
        jPanelSearch.add(button);
    }

    private void setBlankInSearchPanel(JPanelOpen jPanelSearch, String names[]) {
        Set<String> comboxSet = comboxMap.keySet();

        for (String name : names) {
            JPanelOpen jPanelO = new JPanelOpen();
            JLabelOpen jLO = new JLabelOpen();
            if (name.equals("顾客") || name.equals("经手员工")) continue;//dirty code

            jLO.setText(name);

            if (comboxSet.contains(name)) {
                JComboBox<String> stringJComboBox = getComboBox(name);
                jPanelO.add(jLO);
                jPanelO.add(stringJComboBox);
            } else {
                JTextField jTextField = new JTextField();
                jTextField.setColumns(11);
                jPanelO.add(jLO);
                jPanelO.add(jTextField);
            }

            jPanelSearch.add(jPanelO);
        }
    }

    private HashMap<String, String[]> comboxMap = new HashMap<String, String[]>() {{
        put("车况", new String[]{"", "1", "2", "3", "4", "5"});
        put("是否会员", new String[]{"", "Y", "N"});
        put("权限等级", new String[]{"", "1", "2", "3"});
        put("事件", new String[]{"", "损坏维修", "罚款", "借车", "还车"});
    }};

    private JComboBox<String> getComboBox(String name) {
        JComboBox<String> jComboBox = null;
        String[] contentStrings = comboxMap.get(name);
        if (contentStrings != null) {
            jComboBox = new JComboBox<>(contentStrings);
        }
        return jComboBox;
    }


    /**
     * 展示信誉度统计
     */
    private void showCredit(){
        HashMap<String, Float> dataMap = null;
        HashMap<String, String> nameMap = null;

        try {
            dataMap = DataBase.getInstance().getCredit();
            nameMap = DataBase.getInstance().getMapId2Name();
        } catch (SQLException e) {
            noticeMsg("数据库查询信誉度时发生错误");
            e.printStackTrace();
        }
        if (dataMap == null || nameMap == null) return;

        JDialog dialog = new JDialog(frame);
        dialog.setContentPane(new JPanel());
        dialog.getContentPane().setLayout(new BorderLayout());

        Set<String> keys = dataMap.keySet();
        Vector<String> columns = new Vector<>(Arrays.asList("id", "姓名", "信誉值"));
        Vector<Vector<String>> datas = new Vector<>();
        for (String key: keys) {
            datas.add(new Vector<>(Arrays.asList(key,String.valueOf(nameMap.get(key)),String.valueOf(dataMap.get(key)))));
        }

        DefaultTableModel model = new DefaultTableModel(datas,columns);
        JTable creditTable = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(JLabel.CENTER);
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        creditTable.setDefaultRenderer(Object.class,tcr);
        creditTable.getTableHeader().setReorderingAllowed(false);
        creditTable.setModel(model);

        JScrollPane jScrollPane = new JScrollPane(creditTable);
        dialog.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        dialog.pack();
        setCenter(dialog);
        dialog.setTitle("信誉度统计");
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * 加载全新的表格
     * after called, scroll panel would be reset, you should add scroll panel to content panel again.
     *
     * @param vectors data
     * @param columns columns' names
     */
    private void setTablePanel(Vector<Vector<String>> vectors, Vector<String> columns) {
        if (scrollPane != null) {
            jMainPanel.remove(scrollPane);
            scrollPane = null;
        }

        tableModel = new DefaultTableModel(vectors, columns) {
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (updateData(aValue, row, column)) {
                    super.setValueAt(aValue, row, column);//在这里做修改值的限定
                }
            }
        };

        /*
          修改权限的体现
          */
        if ((PANEL_MODE.equals("车辆") || PANEL_MODE.equals("事件")) && authority == 0) {//顾客不能修改车辆表和事件表
            jTable = new JTable() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        } else if (PANEL_MODE.equals("用户") & authority != 0) {//除了超级管理员，不能修改权限和绑定顾客
            jTable = new JTable() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column != 2 && column != 3;
                }
            };
        } else if (PANEL_MODE.equals("事件") && authority != 2) {//修改事件表的时候在顾客和经手员工的地方，只能修改对应id
            jTable = new JTable() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column != 4 && column != 9 && column != 0;
                }
            };
        } else if (columns.get(0).equals("id")) {//id不能被修改
            jTable = new JTable() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column != 0;
                }
            };

        } else {
            jTable = new JTable() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return true;
                }
            };
        }
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(JLabel.CENTER);
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        jTable.setDefaultRenderer(Object.class, tcr);
        jTable.getTableHeader().setReorderingAllowed(false);
        jTable.setModel(tableModel);
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mouseRightButtonClick(e, jTable);
            }
        });

        scrollPane = new JScrollPane(jTable);
        scrollPane.setPreferredSize(new Dimension(1000, 400));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    /**
     * 检查单项数据是否格式合法
     *
     * @param name attribute's name
     * @param value just value
     * @return yes or no
     */
    private boolean checkDataLegal(String name, String value) {
        //check is legal or not
        if (value.contains("'") || value.contains(" ") || value.contains("=")) {
            noticeMsg("新数据中含有非法字段(\"'\", \" \"), \"=\"");
            return false;
        }
        if (name.equals("事件")) {
            if (DataNameUtils.eventName.indexOf(value) == -1) {
                noticeMsg("事件只有四种类型：借车、还车、损坏维修、罚款");
                return false;//不合法事件代码
            }
        }
        if (name.equals("时间")) {
            if (value.length() != 10 || value.charAt(4) != '-' || value.charAt(4) != '-' || Pattern.compile("[^\\d-]").matcher(value).find()) {
                noticeMsg("时间格式错误");
                return false;
            }
        }
        if (name.equals("车牌号")) {
            if (value.length() != 6) {
                noticeMsg("车牌号格式错误");
                return false;
            }
        }
        if (name.equals("车况")) {
            if (Pattern.compile("[^\\d]+").matcher(value).find()) {//保证全数字，防止后面强转出错
                noticeMsg("只能输入数字");
                return false;
            }
            if (Integer.valueOf(value) < 1 || Integer.valueOf(value) > 5) {
                noticeMsg("只能输入1-5的数字");
                return false;
            }
        }
        if (name.equals("是否会员")) {
            if (!value.equals("Y") && !value.equals("N")) {
                noticeMsg("只能输入Y或N");
                return false;
            }
        }
        if (name.equals("权限等级")) {
            if (Pattern.compile("[^\\d]+").matcher(value).find()) {//保证全数字，防止后面强转出错
                noticeMsg("只能输入数字");
                return false;
            }
            int valueInt = Integer.valueOf(value);
            if (valueInt < 1 || valueInt > 3) {
                noticeMsg("只能输入1-3的数字");
                return false;
            }
            if (valueInt != 3 && authority != 1) {
                noticeMsg("无权限操作");
                return false;
            }
        }

        return true;
    }

    //判断是否为鼠标的BUTTON3按钮，BUTTON3为鼠标右键
    private void mouseRightButtonClick(MouseEvent evt, JTable jTable) {

        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            //通过点击位置找到点击为表格中的行
            int focusedRowIndex = jTable.rowAtPoint(evt.getPoint());
            if (focusedRowIndex == -1) {
                return;
            }
            delete_row_id = focusedRowIndex;
            //将表格所选项设为当前右键点击的行
            jTable.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
            //弹出菜单
            if (jPopupMenu != null) {
                jPopupMenu.show(jTable, evt.getX(), evt.getY());
            }
        }
    }


    /**
     * 修改表格界面的监听器
     */
    private ActionListener changeTableListener = e -> {
        String strClick = e.getActionCommand();
        System.out.println(strClick);
        if (!PANEL_MODE.equals(strClick)) {//界面如已加载过则不继续加载
            PANEL_MODE = strClick;
            frame.getContentPane().removeAll();//移除原有的东西

            Vector<Vector<String>> vectors = null;//数据vertor

            try {
                switch (strClick) {
                    case "顾客":
                        columns = new Vector<>(Arrays.asList(DataNameUtils.customerColumns));
                        vectors = DataBase.getInstance().getCustomerLists();
                        break;
                    case "车辆":
                        columns = new Vector<>(Arrays.asList(DataNameUtils.carColumns));
                        vectors = DataBase.getInstance().getCarLists();
                        break;
                    case "员工":
                        columns = new Vector<>(Arrays.asList(DataNameUtils.stuffColumns));
                        vectors = DataBase.getInstance().getStuffLists();
                        break;
                    case "用户":
                        columns = new Vector<>(Arrays.asList(DataNameUtils.usersColumns));
                        vectors = DataBase.getInstance().getUserLists(authority, userName);
                        break;
                    case "事件":
                        columns = new Vector<>(Arrays.asList(DataNameUtils.infoColumns));
                        vectors = DataBase.getInstance().getInfoLists(authority, userName);
                        break;
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
                noticeMsg("数据库发生错误");
            }

            if (vectors != null && columns != null) {
                setTablePanel(vectors, columns);
                //重新渲染整个界面
                JPanelOpen jPanelSearch = new JPanelOpen();
                setSearchPanel(jPanelSearch);
                jMainPanel.add(jPanelSearch, BorderLayout.CENTER);
                jMainPanel.add(scrollPane, BorderLayout.SOUTH);
                jMainPanel.updateUI();
            }

        }
    };

    private JPopupMenu jPopupMenu;

    //右键菜单
    private void createPopupMenu() {
        if (authority == 3) return;
        jPopupMenu = new JPopupMenu();

        JMenuItem delMenuItem = new JMenuItem();
        delMenuItem.setText("删除本行");
        delMenuItem.addActionListener(evt -> {
            String key = (String) jTable.getValueAt(delete_row_id, 0);
            try {
                DataBase.getInstance().deleteRow(PANEL_MODE, key);
                tableModel.removeRow(delete_row_id);
            } catch (SQLException e) {
                e.printStackTrace();
                noticeMsg("删除失败");
            }
        });

        JMenuItem addMenuItem = new JMenuItem();
        addMenuItem.setText("添加新行");
        addMenuItem.addActionListener(evt -> setAddRowDialog());

        jPopupMenu.add(delMenuItem);
        jPopupMenu.add(addMenuItem);
    }

    private JDialog dialogAddRow;

    private void setAddRowDialog() {
        //create a dialog
        dialogAddRow = new JDialog(frame);
        dialogAddRow.setTitle("添加");
        dialogAddRow.setLayout(new FlowLayout());
        JPanel contentPanel = new JPanel();
        dialogAddRow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] columnNames = DataNameUtils.getColumnNamesByMode(PANEL_MODE);//从对应的静态工具类中读取列表行名

        Set<String> comboxSet = comboxMap.keySet();

        for (String s : columnNames) {
            if (!s.equals("id") && !s.equals("顾客") && !s.equals("经手员工")) {
                JPanel jPanel = new JPanel();
                JLabel jLabel = new JLabel(s);
                jPanel.add(jLabel, BorderLayout.WEST);
                if (comboxSet.contains(s)) {
                    jPanel.add(getComboBox(s), BorderLayout.EAST);
                } else {
                    JTextField jTextField = new JTextField(10);
                    jPanel.add(jTextField, BorderLayout.EAST);
                }
                contentPanel.add(jPanel);
            }
        }
        JButton jButton = new JButton("确认");


        jButton.addActionListener(e -> {
            //set this hashmap
            HashMap<String, String> map = getDataMap((JPanel) dialogAddRow.getContentPane());
            if (checkNewData(map)) {
                try {
                    DataBase.getInstance().addRow(PANEL_MODE, map);//单例模式获取数据库管理对象，调用对应的添加行函数
                    tableModel.addRow(map2vector(map));
                    dialogAddRow.dispose();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    noticeMsg("新数据不合法或数据库发生错误");
                }
            }
        });

        contentPanel.add(jButton);
        dialogAddRow.setContentPane(contentPanel);
        dialogAddRow.pack();
        setCenter(dialogAddRow);
    }

    private boolean checkNewData(HashMap<String, String> map) {
        Set<String> set = map.keySet();
        for (String key : set) {
            String value = map.get(key);
            if (!checkDataLegal(key, value)) {//一个个检查
                return false;
            }
        }
        return true;
    }

    private boolean checkSearchData(HashMap<String, String> map) {
        Set<String> set = map.keySet();
        for (String key : set) {
            String value = map.get(key);
            if (value.equals("")) continue;
            if (!checkDataLegal(key, value)) {//一个个检查
                return false;
            }
        }
        return true;
    }

    /**
     * 更新表格数据
     */
    private boolean updateData(Object aValue, int row, int column) {

        String[] columnNames = DataNameUtils.getColumnNamesByMode(PANEL_MODE);
        try {
//            if (checkDataLegal(columnNames[column], aValue.toString(), (String) jTable.getValueAt(row, 0))) {
            if (checkDataLegal(columnNames[column], aValue.toString())) {
                DataBase.getInstance().updateData(PANEL_MODE, columnNames[column], aValue.toString(), (String) jTable.getValueAt(row, 0));
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            noticeMsg("新数据格式不合法");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获得用户填入的新行的数据
     * 传入一个JPanel父容器
     * 传出的字符串可能有带空
     *
     * @return map filled with data
     */
    private HashMap<String, String> getDataMap(JPanel jPanelIn) {
        HashMap<String, String> map = new HashMap<>();
        Component[] components = jPanelIn.getComponents();
        for (int i = 0; i < components.length - 2; i++) {//不算button
            JPanel jPanelGet = (JPanel) components[i];
            String key = ((JLabel) jPanelGet.getComponents()[0]).getText();
            if (key.equals("顾客") || key.equals("经手员工")) {
                continue;
            }

            String value;
            try {
                value = ((JTextField) jPanelGet.getComponents()[1]).getText();
            } catch (Exception e) {
                value = (String) ((JComboBox) jPanelGet.getComponents()[1]).getSelectedItem();
            }
            map.put(key, value);
            System.out.println("key: " + key + "; value: " + value);
        }
        return map;
    }

    /**
     * hashmap转vector，方便addRow
     *
     * @param map old hashmap
     * @return new vector
     */
    private Vector<String> map2vector(HashMap<String, String> map) {
        Vector<String> vector = new Vector<>();

        String[] columnNames = DataNameUtils.getColumnNamesByMode(PANEL_MODE);
        for (String s : columnNames) {
            String v = map.get(s);
            if (v != null) {
                vector.add(v);
            } else {
                vector.add("");
            }
        }

        return vector;
    }

}
