package test;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;

public class DOMParser 
{
	static WebDriver driver;
	static WebDriverWait wait;
	
	public static void RunInLoop() throws Exception
	{
		
		JOptionPane.showMessageDialog(null, "Please click OK when you arrive at the required page.");
		
		System.out.println("=====================================================================================================================");
		List<WebElement> allElements = driver.findElements(By.xpath("//*[contains(@formcontrolname,'')]"));		
		ArrayList<WebElement> myElements = new ArrayList<WebElement>();
		ArrayList<String> allLocatorNames = new ArrayList<String>();
				
		for(WebElement w : allElements)
		{
			String s = w.getAttribute("formcontrolname");
			if(s!=null)
			{
				myElements.add(w);
			}
		}
		System.out.println();
		StringBuffer sbuf = new StringBuffer();
		String currentLocatorName, formcontrolname;
		for(WebElement w : myElements)
		{
			formcontrolname = w.getAttribute("formcontrolname");
			currentLocatorName = formcontrolname.substring(0,1).toUpperCase() + formcontrolname.substring(1, formcontrolname.length());
			
			switch(w.getTagName())
			{
			case "input": 
				currentLocatorName = "txt"+currentLocatorName;
				sbuf.append("private readonly By "+currentLocatorName+" = Via.FormControlName(\""+w.getAttribute("formcontrolname")+"\");\n");
				break;
				
			case "mat-select":
				if(w.getAttribute("aria-multiselectable").trim().equalsIgnoreCase("false"))
				{
					currentLocatorName = "ddl"+currentLocatorName;
					sbuf.append("private readonly By "+currentLocatorName+" = Via.FormControlName(\""+w.getAttribute("formcontrolname")+"\");\n");
				}
				else
				{
					currentLocatorName = "ddm"+currentLocatorName;
					sbuf.append("private readonly By "+currentLocatorName+" = Via.FormControlName(\""+w.getAttribute("formcontrolname")+"\");\n");
				}
				break;
				
			case "textarea": 
				currentLocatorName = "txa"+currentLocatorName;
				sbuf.append("private readonly By "+currentLocatorName+" = Via.FormControlName(\""+w.getAttribute("formcontrolname")+"\");\n");
				break;
				
			case "mat-radio-group": 
				currentLocatorName = "rdo"+currentLocatorName;
				sbuf.append("private readonly By "+currentLocatorName+" = Via.FormControlName(\""+w.getAttribute("formcontrolname")+"\");\n");
				break;
				
			case "mat-slide-toggle": 
				currentLocatorName = "tgl"+currentLocatorName;
				sbuf.append("private readonly By "+currentLocatorName+" = Via.FormControlName(\""+w.getAttribute("formcontrolname")+"\");\n");
				break;
			}
			allLocatorNames.add(currentLocatorName);
		}
		
		System.out.println(sbuf.toString());				
		System.out.println("\n");
		StringBuffer sss = new StringBuffer();		
		for(int i=0; i<allLocatorNames.size(); i++)
		{
			if(i!=0)
				sss.append(",");
			sss.append(allLocatorNames.get(i));
		}		
		POMCreator.PrintMethods(sss.toString());
		
		System.out.println("=====================================================================================================================");
	}
	
	static String correctedElements = "";
	static JFrame frame;
	public static void ShowMessage()
	{
		String inputElements = "txtName,ddlCategoryReferenceItemRefKey,ddmAssessmentRefKeys,txtCmnDescription,ddlPriceCodeRefKey,ddlRevenueCodeRefKey,ddlUb04RevenueCodeRefKey,tglIsTaxable,ddlDispensingUnitReferenceItemRefKey,ddlTherapyTypeReferenceItemRefKey,txtPricePerDispensableUnit,txtHcpcCode,txtHcpcCodeReferenceItemRefKey,ddlHcpcUnitOfMeasureReferenceItemRefKey,txtHcpcDescription";
		frame = new JFrame("Please confirm web elements");
        JTextArea ta = new JTextArea(30, 60);
        String[] a = inputElements.split(",");
        StringBuffer sbuf = new StringBuffer();
        for(String a1 : a)
        	sbuf.append(a1+"\n");
        ta.setText(sbuf.toString());
        JScrollPane sp = new JScrollPane(ta);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 660);
        JButton proceed = new JButton("Proceed");
        frame.getContentPane().add(sp);
        frame.getContentPane().add(proceed);
        frame.setVisible(true);        
        proceed.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				correctedElements = ta.getText();
				frame.setVisible(false);
			}
		});
	}
	
	
	
	public static void main(String args[]) throws Exception
	{		
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://www.caretend.io");		
		wait = new WebDriverWait(driver, 120);		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='username']"))).sendKeys("echo.qa@mediware.com");;
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='password']"))).sendKeys("KorbenDallas@Med1w4re");;
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class,'next-button')]"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class,'next-button')]/span[contains(text(),'SIGN IN')]"))).click();
		while(true)
		{
			try
			{
				RunInLoop();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
