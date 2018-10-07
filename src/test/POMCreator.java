package test;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class POMCreator 
{
	
	static String correctedElements = "";
	static JFrame frame;
	public static void ShowMessage(ArrayList<Locator> allLocators) throws Exception
	{
		//String inputElements = "txtName,ddlCategoryReferenceItemRefKey,ddmAssessmentRefKeys,txtCmnDescription,ddlPriceCodeRefKey,ddlRevenueCodeRefKey,ddlUb04RevenueCodeRefKey,tglIsTaxable,ddlDispensingUnitReferenceItemRefKey,ddlTherapyTypeReferenceItemRefKey,txtPricePerDispensableUnit,txtHcpcCode,txtHcpcCodeReferenceItemRefKey,ddlHcpcUnitOfMeasureReferenceItemRefKey,txtHcpcDescription";
		frame = new JFrame("Please confirm web elements");
        JTextArea ta = new JTextArea(30, 60);
        
        //String[] a = inputElements.split(",");
        StringBuffer sbuf = new StringBuffer();
        for(Locator a1 : allLocators)
        	sbuf.append(a1.locatorName + "\n");
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
        while(true)
        {
        	if(frame.isVisible())
        		Thread.sleep(1000);
        	else
        		break;
        }        
	}
	
	public static void PrintMethods(ArrayList<Locator> allLocators) throws Exception
	{		
		ShowMessage(allLocators);
		
		boolean found;
		for(String a : correctedElements.split("\n"))
		{
			found = false;
			for(Locator l : allLocators)
				if(a.equalsIgnoreCase(l.locatorName))
				{
					System.out.println(l.completeLocator);
					found=true;
					break;
				}
			
			if(found==false && a.startsWith("lbl"))
			{
				System.out.println("private readonly By "+a+" = Via.LabelFor(\"\");");
				found = true;
			}
			
			if(found==false)
			{
				String onlyName = a.substring(3, a.length());
				for(Locator l : allLocators)
				{
					if(l.locatorName.substring(3, l.locatorName.length()).equalsIgnoreCase(onlyName))
					{
						System.out.println(l.completeLocator);
						found=true;
						break;
					}
				}
			}
		}
		
		System.out.println();
		
		ArrayList<Element> myElements = new ArrayList<Element>();
		
		for(String a : correctedElements.split("\n"))
		{
			Element element = new Element();
			element.locator = a;
			element.elementType = a.substring(0, 3);
			String pname = a.substring(3, a.length());
			element.parameterName = pname.substring(0,1).toLowerCase() + pname.substring(1, pname.length());
			switch(element.elementType)
			{//rdoOrderType
			case "txq":
				element.methodActionName = "Enter" + a.substring(3, a.length()); 
				element.methodValidationName = "Validate" + a.substring(3, a.length()) + "State"; 
				element.parameterType = "string"; 
				System.out.println(QuantityBoxOperation(element));
				break;
			
			case "txp":
				element.methodActionName = "Enter" + a.substring(3, a.length()); 
				element.methodValidationName = "Validate" + a.substring(3, a.length()) + "State"; 
				element.parameterType = "string"; 
				System.out.println(PriceBoxOperation(element));
				break;
				
			case "txa": 
				element.methodActionName = "Enter" + a.substring(3, a.length()); 
				element.methodValidationName = "Validate" + a.substring(3, a.length()) + "State"; 
				element.parameterType = "string"; 
				System.out.println(TextAreaOperation(element));
				break;
				
			case "txt": 
				element.methodActionName = "Enter" + a.substring(3, a.length()); 
				element.methodValidationName = "Validate" + a.substring(3, a.length()) + "State"; 
				element.parameterType = "string"; 
				System.out.println(TextBoxOperation(element));
				break;
				
			case "ddl":
				element.methodActionName = "Select" + a.substring(3, a.length()); 
				element.methodValidationName = "Validate" + a.substring(3, a.length()) + "State"; 
				element.parameterType = "string"; 
				System.out.println(DropdownOperation(element));
				break;
				
			case "ddm": 
				element.methodActionName = "Select" + a.substring(3, a.length()); 
				element.methodValidationName = "Validate" + a.substring(3, a.length()) + "State"; 
				element.parameterType = "string[]"; 
				System.out.println(MultiSelectDropdownOperation(element));
				break;
				
			case "btn": 
				element.methodActionName = "Click" + a.substring(3, a.length()) + "Button"; 
				element.parameterType = ""; 
				System.out.println(ButtonOperation(element));
				break;
				
			case "tgl": 
				element.methodActionName = "Toggle" + a.substring(3, a.length()); 
				element.parameterType = "bool"; 
				System.out.println(ToggleOperation(element));
				break;
				
			case "rdo": 
				element.methodActionName = "Select" + a.substring(3, a.length()); 
				element.parameterType = "string"; 
				System.out.println(RadioButtonOperation(element));
				break;
				
			case "lbl":
				element.methodValidationName = "Validate" + a.substring(3, a.length()); 
				element.parameterType = "string";
				System.out.println(LabelSingleOperation(element));
				break;
				
			case "lbm":
				element.methodValidationName = "Validate" + a.substring(3, a.length()); 
				element.parameterType = "string[]";
				System.out.println(LabelMultiOperation(element));
				break;
				
			case "chk":
				element.methodValidationName = "Validate" + a.substring(3, a.length()); 
				element.parameterType = "bool";
				System.out.println(CheckOperation(element));
				break;
			}
			
			myElements.add(element);
		}
	}
	
	public static String CheckOperation(Element e)
	{
		StringBuffer s = new StringBuffer();
		s.append("public void "+e.methodValidationName+"("+e.parameterType+" "+e.parameterName+")\n");
		s.append("{\n");
		s.append("Actions.ValidateCheck("+e.locator+", "+e.parameterName+");\n");
		s.append("}\n");
		return s.toString();
	}
	
	public static String LabelMultiOperation(Element e)
	{
		StringBuffer s = new StringBuffer();
		s.append("public void "+e.methodValidationName+"("+e.parameterType+" "+e.parameterName+")\n");
		s.append("{\n");
		s.append("Actions.ValidateTextIs("+e.locator+", "+e.parameterName+");\n");
		s.append("}\n");
		return s.toString();
	}
	
	public static String LabelSingleOperation(Element e)
	{
		StringBuffer s = new StringBuffer();
		s.append("public void "+e.methodValidationName+"("+e.parameterType+" "+e.parameterName+")\n");
		s.append("{\n");
		s.append("Actions.ValidateTextIs("+e.locator+", "+e.parameterName+");\n");
		s.append("}\n");
		return s.toString();
	}	
	
	public static String RadioButtonOperation(Element e)
	{
		StringBuffer s = new StringBuffer();
		s.append("public "+e.parameterType+" "+e.methodActionName+"()\n");
		s.append("{\n");
		s.append("return Actions.SelectRandomlyRadioButton(\"//mat-radio-group[@formcontrolname = '"+e.parameterName+"']\");\n");
		s.append("}\n");
		return s.toString();
	}
	
	
	public static String ToggleOperation(Element e)
	{
		StringBuffer s = new StringBuffer();
		s.append("public "+e.parameterType+" "+e.methodActionName+"()\n");
		s.append("{\n");
		s.append("return Actions.SelectRandomlyToggleButton("+e.locator+");\n");
		s.append("}\n\n");
		s.append("public void "+e.methodActionName+"("+e.parameterType+" "+e.parameterName+")\n");
		s.append("{\n");
		s.append("Actions.SetToggle("+e.locator+", "+e.parameterName+");\n");
		s.append("}\n");
		return s.toString();
	}
	
	public static String ButtonOperation(Element e)
	{
		StringBuffer s = new StringBuffer();
		s.append("public void "+e.methodActionName+"()\n");
		s.append("{\n");
		s.append("Waits.WaitForElementToBeClickable("+e.locator+", WaitType.Small);\n");
		s.append("Actions.Click("+e.locator+");\n");
		s.append("return CreateInstance<>();\n");
		s.append("}\n");
		return s.toString();
	}
	
	public static String MultiSelectDropdownOperation(Element e)
	{
		StringBuffer s = new StringBuffer();
		s.append("public "+e.parameterType+" "+e.methodActionName+"()\n");
		s.append("{\n");
		s.append("return Actions.SelectRandomlyFromMultiSelectDropdown("+e.locator+");\n");
		s.append("}\n\n");
		s.append("public void "+e.methodActionName+"("+e.parameterType+" "+e.parameterName+")\n");
		s.append("{\n");
		s.append("Actions.SelectFromDropdown("+e.locator+", "+e.parameterName+");\n");
		s.append("}\n\n");
		s.append("public void "+e.methodValidationName+"(bool isValid)\n");
		s.append("{\n");
		s.append("Actions.ValidateElementState("+e.locator+", isValid);\n");
		s.append("}\n");
		return s.toString();
	}
	
	public static String DropdownOperation(Element e)
	{
		StringBuffer s = new StringBuffer();
		s.append("public "+e.parameterType+" "+e.methodActionName+"()\n");
		s.append("{\n");
		s.append("return Actions.SelectRandomlyFromSingleSelectDropdown("+e.locator+");\n");
		s.append("}\n\n");
		s.append("public void "+e.methodActionName+"("+e.parameterType+" "+e.parameterName+")\n");
		s.append("{\n");
		s.append("Actions.SelectFromDropdown("+e.locator+", "+e.parameterName+");\n");
		s.append("}\n\n");
		s.append("public void "+e.methodValidationName+"(bool isValid)\n");
		s.append("{\n");
		s.append("Actions.ValidateElementState("+e.locator+", isValid);\n");
		s.append("}\n");
		return s.toString();
	}
	
	public static String TextBoxOperation(Element e)
	{
		StringBuffer s = new StringBuffer();
		s.append("public "+e.parameterType+" "+e.methodActionName+"()\n");
		s.append("{\n");
		s.append("string "+e.parameterName+" = CommonMethods.GenerateRandomAlphanumericString(40);\n");
		s.append(e.methodActionName + "("+e.parameterName+");\n");
		s.append("return " + e.parameterName + ";\n");
		s.append("}\n\n");		
		s.append("public void "+e.methodActionName+"("+e.parameterType+" "+e.parameterName+")\n");
		s.append("{\n");
		s.append("Waits.WaitForElementToBeClickable("+e.locator+", WaitType.Small);\n");
		s.append("Actions.Clear("+e.locator+");\n");
		s.append("Actions.SendKeys("+e.locator+", "+e.parameterName+");\n");
		s.append("}\n\n");
		s.append("public void "+e.methodValidationName+"(bool isValid)\n");
		s.append("{\n");
		s.append("Actions.ValidateElementState("+e.locator+", isValid);\n");
		s.append("}\n");
		return s.toString();
	}
	
	public static String TextAreaOperation(Element e)
	{
		StringBuffer s = new StringBuffer();
		s.append("public "+e.parameterType+" "+e.methodActionName+"()\n");
		s.append("{\n");
		s.append("string "+e.parameterName+" = CommonMethods.GenerateRandomAlphanumericString(100);\n");
		s.append(e.methodActionName + "("+e.parameterName+");\n");
		s.append("return " + e.parameterName + ";\n");
		s.append("}\n\n");		
		s.append("public void "+e.methodActionName+"("+e.parameterType+" "+e.parameterName+")\n");
		s.append("{\n");
		s.append("Waits.WaitForElementToBeClickable("+e.locator+", WaitType.Small);\n");
		s.append("Actions.Clear("+e.locator+");\n");
		s.append("Actions.SendKeys("+e.locator+", "+e.parameterName+");\n");
		s.append("}\n\n");
		s.append("public void "+e.methodValidationName+"(bool isValid)\n");
		s.append("{\n");
		s.append("Actions.ValidateElementState("+e.locator+", isValid);\n");
		s.append("}\n");
		return s.toString();
	}
	
	public static String PriceBoxOperation(Element e)
	{
		StringBuffer s = new StringBuffer();
		s.append("public "+e.parameterType+" "+e.methodActionName+"()\n");
		s.append("{\n");
		s.append("string "+e.parameterName+" = \"\" + CommonMethods.GenerateRandomNumber(1, 999) + (CommonMethods.GenerateRandomNumber(1, 9999)) / 10000.0000;\n");
		s.append(""+e.methodActionName+"("+e.parameterName+");\n");
		s.append("return "+e.parameterName+";\n");
		s.append("}\n\n");
		s.append("public void "+e.methodActionName+"("+e.parameterType+" "+e.parameterName+")\n");
		s.append("{\n");
		s.append("Waits.WaitForElementToBeClickable("+e.locator+", WaitType.Small);\n");
		s.append("Actions.Clear("+e.locator+");\n");
		s.append("Actions.SendKeys("+e.locator+", "+e.parameterName+");\n");
		s.append("}\n\n");
		s.append("public void "+e.methodValidationName+"(bool isValid)\n");
		s.append("{\n");
		s.append("Actions.ValidateElementState("+e.locator+", isValid);\n");
		s.append("}\n");
		return s.toString();
	}
	
	public static String QuantityBoxOperation(Element e)
	{
		StringBuffer s = new StringBuffer();
		s.append("public string "+e.methodActionName+"()\n");
		s.append("{\n");
		s.append("string "+e.parameterName+" = CommonMethods.GenerateRandomNumber(1, 999) + \"\";\n");
		s.append(""+e.methodActionName+"("+e.parameterName+");\n");
		s.append("return "+e.parameterName+";\n");
		s.append("}\n\n");
		s.append("public void "+e.methodActionName+"(string "+e.parameterName+")\n");
		s.append("{\n");
		s.append("Waits.WaitForElementToBeClickable("+e.locator+", WaitType.Small);\n");
		s.append("Actions.Clear("+e.locator+");\n");
		s.append("Actions.SendKeys("+e.locator+", "+e.parameterName+");\n");
		s.append("}\n\n");
		s.append("public void "+e.methodValidationName+"(bool isValid)\n");
		s.append("{\n");
		s.append("Actions.ValidateElementState("+e.parameterName+", isValid);\n");
		s.append("}\n");
		return s.toString();
	}
}
