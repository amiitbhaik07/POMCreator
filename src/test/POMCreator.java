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
	public static void ShowMessage(String inputElements) throws Exception
	{
		//String inputElements = "txtName,ddlCategoryReferenceItemRefKey,ddmAssessmentRefKeys,txtCmnDescription,ddlPriceCodeRefKey,ddlRevenueCodeRefKey,ddlUb04RevenueCodeRefKey,tglIsTaxable,ddlDispensingUnitReferenceItemRefKey,ddlTherapyTypeReferenceItemRefKey,txtPricePerDispensableUnit,txtHcpcCode,txtHcpcCodeReferenceItemRefKey,ddlHcpcUnitOfMeasureReferenceItemRefKey,txtHcpcDescription";
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
        while(true)
        {
        	if(frame.isVisible())
        		Thread.sleep(1000);
        	else
        		break;
        }        
	}
	
	public static void PrintMethods(String inputElements) throws Exception
	{
		System.out.println("//"+inputElements);
		
		ShowMessage(inputElements);
		
		//String[] allElements = inputElements.split(",");
		
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
			}
			
			myElements.add(element);
		}
		
	
	}
	
	/*public static void main(String[] args) 
	{
		String inputElements = "txtProductName,txtBrandName,txtGenericName,ddlManufacturer,txtNdc,ddlNdcUnitQualifier,txtStrength,ddlDispensableUnitAndHcpcUom,txtDispensableUnitQuantity,txtHcpcUomQuantity,ddlIndividualUnitOfMeasure,txtCaloriesPerIndividualUom,txtMlPerCcPerIndividualUom,lblCaloriesPerMlPerCc,ddmAssessment,ddmAssignedSupplyKit,txtProductAlertNotes,rdoOrderType,tglTrackInventory,tglTrackLots,tglTrackSerialNumber,tglSelectSerialNumbersWhenAddingToDeliveryTicket,tglRequiresRentPurchaseLetterAtFirstMonth,ddlAssetSettingsProfile,txtDaysBetweenChecks,txtDaysBetweenPreventiveMaintenance,txtDepricationLifespan,ddlReturnStatus,ddlEquipmentProfile,tglIvContainer,ttxtContainerVolume,btnNext";
		
		String[] allElements = inputElements.split(",");
		
		ArrayList<Element> myElements = new ArrayList<Element>();
		
		for(String a : allElements)
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
			}
			
			myElements.add(element);
		}
		
	}*/
	
	
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
		s.append("public "+e.parameterType+" "+e.methodActionName+"()\n");
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
