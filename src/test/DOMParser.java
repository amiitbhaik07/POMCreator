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
		List<WebElement> allFormControlNameElements = driver.findElements(By.xpath("//*[contains(@formcontrolname,'')]"));		
		List<WebElement> allNgReflectNameElements = driver.findElements(By.xpath("//*[contains(@ng-reflect-name,'')]"));
		List<WebElement> allCheckFlagElements = driver.findElements(By.xpath("//app-check-close[contains(@ng-reflect-is-check, 'true') or contains(@ng-reflect-is-check, 'false')]"));
		List<WebElement> allButtonElements = driver.findElements(By.xpath("//button[@id!='']"));
		ArrayList<WebElement> myElements = new ArrayList<WebElement>();
		ArrayList<String> allLocatorNames = new ArrayList<String>();
				
		for(WebElement w : allFormControlNameElements)
		{
			String s = w.getAttribute("formcontrolname");
			if(s!=null)
			{
				myElements.add(w);
			}
		}
		
		System.out.println("Ng reflect name element count : " + allNgReflectNameElements.size());
		String tagName = "";
		int ngreflectcounter = 0;
		for(WebElement w : allNgReflectNameElements)
		{
			if(w.getAttribute("formcontrolname")==null || w.getAttribute("formcontrolname").equals(""))
			{
				tagName = w.getTagName();
				if(tagName.equalsIgnoreCase("mat-slide-toggle"))
				{
					myElements.add(w);
					ngreflectcounter++;
				}
			}
		}
		
		for(WebElement w : allCheckFlagElements)
		{
			myElements.add(w);
		}
		
		ArrayList buttonIds = new ArrayList();
		for(WebElement w : allButtonElements)
		{
			if(!buttonIds.contains(w.getAttribute("id")))
			{
				buttonIds.add(w.getAttribute("id"));
				myElements.add(w);
			}
		}


		StringBuffer sbuf = new StringBuffer();
		String currentLocatorName, formcontrolname=null;
		
		//0 = formcontrolname
		//1 = ngreflectname
		//3 = Button (id)
		int currentState = 0;
		for(WebElement w : myElements)
		{
			currentState = 0;
			formcontrolname = w.getAttribute("formcontrolname");
			if(formcontrolname==null || formcontrolname.equals(""))
			{
				formcontrolname = w.getAttribute("ng-reflect-name");
				currentState = 1;
			}
			if(formcontrolname==null || formcontrolname.equals(""))
			{
				formcontrolname = w.findElement(By.xpath("./following::div[1]")).getText();
				currentState = 2;
			}
			if(formcontrolname==null || formcontrolname.equals(""))
			{
				formcontrolname = w.getAttribute("id");
				currentState = 3;
			}
			if(formcontrolname==null || formcontrolname.equals(""))
			{
				continue;
			}
				
			currentLocatorName = formcontrolname.substring(0,1).toUpperCase() + formcontrolname.substring(1, formcontrolname.length()).replace(" ", "").replace("-", "");
			
			switch(w.getTagName())
			{
			case "input": 
				currentLocatorName = "txt"+currentLocatorName;
				break;
				
			case "mat-select":
				if(w.getAttribute("aria-multiselectable").trim().equalsIgnoreCase("false"))
				{
					currentLocatorName = "ddl"+currentLocatorName;
				}
				else
				{
					currentLocatorName = "ddm"+currentLocatorName;
				}
				break;
				
			case "textarea": 
				currentLocatorName = "txa"+currentLocatorName;
				break;
				
			case "mat-radio-group": 
				currentLocatorName = "rdo"+currentLocatorName;
				break;
				
			case "mat-slide-toggle": 
				currentLocatorName = "tgl"+currentLocatorName;
				break;
				
			case "app-check-close":
				currentLocatorName = "chkIs"+currentLocatorName;
				break;
				
			case "button":
				currentLocatorName = "btn"+currentLocatorName;
				break;
			}
			
			switch(currentState)
			{
			case 0: 
				sbuf.append("private readonly By "+currentLocatorName+" = Via.FormControlName(\""+w.getAttribute("formcontrolname")+"\");\n");
				break;
				
			case 1:
				sbuf.append("private readonly By "+currentLocatorName+" = Via.NgReflectName(\""+w.getAttribute("ng-reflect-name")+"\");\n");
				break;
				
			case 2:
				sbuf.append("private readonly By "+currentLocatorName+" = Via.CheckFlagFor(\""+formcontrolname+"\");\n");
				break;
				
			case 3:
				sbuf.append("private readonly By "+currentLocatorName+" = Via.Id(\""+formcontrolname+"\");\n");
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
