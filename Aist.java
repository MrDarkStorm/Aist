/*
	Эта программа и язык Aist созданы Александром Русакевичем(MrDarkStorm)
	Если у вас возникнут вопросы, то обращайтесь:
	Skype: Pro100SanyaRusakevich
*/
import java.io.*;
import java.util.*;
import java.math.*;
import java.util.regex.*;

public class Aist{
	
	static int line_num = 0;
	
	//Список значений строковых переменных
	static List<String> String_Variables = new ArrayList<String>();
	//Список имён строковых переменных
	static List<String> String_Names = new ArrayList<String>();
	//Список значений целочисленных переменных
	static List<Integer> Int_Variables = new ArrayList<Integer>();
	//Список имён целочисленных переменных
	static List<String> Int_Names = new ArrayList<String>();
	//Список значений дробных переменных
	static List<Double> Real_Variables = new ArrayList<Double>();
	//Список имён дробных переменных
	static List<String> Real_Names = new ArrayList<String>();
	//Список значений булевых переменных
	static List<Boolean> Bool_Variables = new ArrayList<Boolean>();
	//Список имён булевых переменных
	static List<String> Bool_Names = new ArrayList<String>();
	//Список меток
	static List<String> Goto_Label_Names = new ArrayList<String>();
	//Список позиций меток
	static List<Integer> Goto_Label_Places = new ArrayList<Integer>();
	
	//Операторы Aist
	static final String[] Aist_opeators = new String[]{"var","variable","int","integer","str","string","real","set","print","println","add","nil","+","sub",
		"-"};
	
	//Отпарсить строку
	static String Aist_ParseString(String s){
		String RetString = "";
		for(int i=0;i<s.length();i++){
			//Escape последовательности
			if(s.charAt(i)=='\\'){
				i++;
				if(i==s.length()){
					System.out.println("EscapeSequenceError: Met by the end of the string, but expected control character Escape sequence. Line "
						+Integer.toString(line_num+1));
						System.exit(1);
				}
				if(s.charAt(i)=='\\'){
					RetString += s.charAt(i);
				}
				else if(s.charAt(i)=='n'){
					RetString += "\n";
				}
				else{
					System.out.println("EscapeSequenceError: Unknown Escape sequence. Line "
						+Integer.toString(line_num+1));
						System.exit(1);
				}
			}else{
				RetString += s.charAt(i);
			}
		}
		return RetString;
	}
	
	static int Aist_ParseInteger(String s){
		int Rets = 0;
		try{
			Rets = Integer.parseInt(s.replaceAll(" ",""));
		}catch(Exception e){
			System.out.println("TypeError: This object is not a number - \'"+s+"\'. Line "
						+Integer.toString(line_num+1));
						System.exit(1);
		}
		return Rets;
	}
	
	static double Aist_ParseReal(String s){
		double Rets = 0;
		try{
			Rets = Double.parseDouble(s.replaceAll(" ","").replace(',','.'));
		}catch(Exception e){
			System.out.println("TypeError: This object is not a number - \'"+s+"\'. Line "
						+Integer.toString(line_num+1));
						System.exit(1);
		}
		return Rets;
	}
	
	static boolean Aist_ParseBool(String s){
		boolean Rets = true;
		if(s.trim().equals("")||s.trim().equals("false")||s.trim().equals("0")){
			Rets = false;
		}
		if(!s.trim().equals("")&&!s.trim().equals("false")&&!s.trim().equals("0")){
			Rets = true;
		}
		return Rets;
	}
	
    public static void main(String[] args){
		//Проверяем правильность количества параметров
        if(args.length!=1){
            System.out.println("ParametersError: Wrong number of command line parameters");
			System.exit(1);
        }
		//Узнать версию программы
		if(args[0].equalsIgnoreCase("-version")){
			System.out.println("Aist version: alpha 0.1");
			System.exit(1);
		}
		//Узнать версию программы
		if(args[0].equalsIgnoreCase("-author")){
			System.out.println("The author of the program and language Aist - Alexander Rusakevich(MrDarkStorm)\n"+
				"If You have any questions, please contact me:\nSkype: Pro100SanyaRusakevich");
			System.exit(1);
		}
		
		List<String> Lines = new ArrayList<String>();
		//Проверяем, всё ли в порядке с файлом. Если да - то считываем данные в файл
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]),"UTF-8"))){
			String s;
			int cur_line = 0;
			while((s=br.readLine())!=null){
				cur_line++;
				Pattern p = Pattern.compile("^-.+$");
				Matcher m = p.matcher(s.trim());
				//Если строка - goto метка, то...
				if(m.matches()){
					String Label_Name = s.trim().substring(1,s.trim().length());
					Goto_Label_Names.add(Label_Name);
					Goto_Label_Places.add(cur_line);
				}
				Lines.add(s.replaceAll("^\\s+", ""));	
			}
		}catch(Exception e){
			System.out.println("OpeningFileError: Cannot open file - no access To it or it does not exist");
			System.exit(1);
		}
		
		//Реализация интерпретации комманд
		while(line_num<Lines.size()){
			if(Lines.get(line_num).trim().equals("")) continue;
			if(Lines.get(line_num).charAt(0)==';') continue;
			
			//Создание новой переменной============================== +
			if(Lines.get(line_num).split("\\s+")[0].equals("variable")||Lines.get(line_num).split("\\s+")[0].equals("var")){
				if(Lines.get(line_num).split("\\s+").length>3){
					System.out.println("ParameterError: Were found the extra parameters at the end of the line. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				if(Lines.get(line_num).split("\\s+").length<3){
					System.out.println("ParameterError: Too few parameters. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Проверить, существует ли уже такая переменная
				if(String_Names.contains(Lines.get(line_num).split("\\s+")[2])||Int_Names.contains(Lines.get(line_num).split("\\s+")[2])||
					Real_Names.contains(Lines.get(line_num).split("\\s+")[2])){
						System.out.println("NameError: The object named \'"+Lines.get(line_num).split("\\s+")[2]
							+"\' already exists. Line "+Integer.toString(line_num+1));
						System.exit(1);
					}
				//Проверить, является ли имя переменной ключевым словом
				for(int i=0;i<Aist_opeators.length;i++){
					if(Lines.get(line_num).split("\\s+")[2].equals(Aist_opeators[i])){
						System.out.println("NameError: The object named \'"+Lines.get(line_num).split("\\s+")[2]
							+"\' is reserved by the interpreter. Line "+Integer.toString(line_num+1));
						System.exit(1);
					}
				}
				if(Lines.get(line_num).split("\\s+")[2].contains("=")||Lines.get(line_num).split("\\s+")[2].contains(":")){
					System.out.println("NameError: Wrong name of variable - \'"+Lines.get(line_num).split("\\s+")[2]
							+"\'. Line "+Integer.toString(line_num+1));
						System.exit(1);
				}
				//Создать новую строковую переменную
				if(Lines.get(line_num).split("\\s+")[1].equals("str")||Lines.get(line_num).split("\\s+")[1].equals("string")){
					String_Names.add(Lines.get(line_num).split("\\s+")[2]);
					String_Variables.add("");
				}
				//Создание новой целочисленной переменной
				else if(Lines.get(line_num).split("\\s+")[1].equals("int")||Lines.get(line_num).split("\\s+")[1].equals("integer")){
					Int_Names.add(Lines.get(line_num).split("\\s+")[2]);
					Int_Variables.add(0);
				}
				//Создание новой дроби
				else if(Lines.get(line_num).split("\\s+")[1].equals("real")){
					Real_Names.add(Lines.get(line_num).split("\\s+")[2]);
					Real_Variables.add(0.0);
				}
				else if(Lines.get(line_num).split("\\s+")[1].equals("bool")||Lines.get(line_num).split("\\s+")[1].equals("boolean")){
					Bool_Names.add(Lines.get(line_num).split("\\s+")[2]);
					Bool_Variables.add(false);
				//Ошибка "Неизвестный тип данных"
				}else{
					System.out.println("TypeError: Unknown data type \'"+Lines.get(line_num).split("\\s+")[1]+"\'. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
			}
			//=======================================================
			
			
			
			
			//Установить значение переменной
			else if(Lines.get(line_num).split("\\s+")[0].equals("set")){
				if(!Lines.get(line_num).contains("=")){
					System.out.println("SyntaxError: The symbol '=' must be in SET. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				String First_Part = Lines.get(line_num).split("=")[0].trim();
				if(First_Part.split("\\s+").length!=2){
					System.out.println("ParameterError: SET must contain a variable name. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				String Var_Name = First_Part.split("\\s+")[1];
				//Проверить существует ли переменная
				if(!String_Names.contains(Var_Name)&&!Int_Names.contains(Var_Name)&&
					!Real_Names.contains(Var_Name)&&!Bool_Names.contains(Var_Name)){
						System.out.println("NameError: Variable does not exist - \'"+Var_Name
							+"\'. Line "+Integer.toString(line_num+1));
						System.exit(1);
					}
				String Set_Value = Lines.get(line_num).substring(Lines.get(line_num).indexOf('=')+1,Lines.get(line_num).length());
				//Присваивание значения строковой переменной
				if(String_Names.contains(Var_Name)){
					String Set_String = Aist_ParseString(Set_Value);
					String_Variables.set(String_Names.indexOf(Var_Name),Set_String);
				}
				//Присваивание значения целочисленной переменной
				else if(Int_Names.contains(Var_Name)){
					int Set_Int = Aist_ParseInteger(Set_Value);
					Int_Variables.set(Int_Names.indexOf(Var_Name),Set_Int);
				}
				//Присваивание значения дроби
				else if(Real_Names.contains(Var_Name)){
					double Set_Real = Aist_ParseReal(Set_Value);
					Real_Variables.set(Real_Names.indexOf(Var_Name),Set_Real);
				}
				else if(Bool_Names.contains(Var_Name)){
					boolean Set_Bool = Aist_ParseBool(Set_Value);
					Bool_Variables.set(Bool_Names.indexOf(Var_Name),Set_Bool);
				}
			}
			
			//Оператор вывода - print
			else if(Lines.get(line_num).split("\\s+")[0].equals("print")){
				if(Lines.get(line_num).split("\\s+").length<1){
					System.out.println("ParameterError: Too few parameters. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				
				for(int i=1;i<Lines.get(line_num).split("\\s+").length;i++){	
					//Проверить существует ли переменная
					if(!String_Names.contains(Lines.get(line_num).split("\\s+")[i])&&!Int_Names.contains(Lines.get(line_num).split("\\s+")[i])&&
					!Real_Names.contains(Lines.get(line_num).split("\\s+")[i])&&!Bool_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						System.out.println("NameError: Variable does not exist - \'"+Lines.get(line_num).split("\\s+")[i]
							+"\'. Line "+Integer.toString(line_num+1));
						System.exit(1);
					}
					
					if(String_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						System.out.print(String_Variables.get(String_Names.indexOf(Lines.get(line_num).split("\\s+")[i])));
					}
					else if(Int_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						System.out.print(Int_Variables.get(Int_Names.indexOf(Lines.get(line_num).split("\\s+")[i])));
					}
					else if(Real_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						System.out.print(Real_Variables.get(Real_Names.indexOf(Lines.get(line_num).split("\\s+")[i])));
					}
					else if(Bool_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						System.out.print(Bool_Variables.get(Bool_Names.indexOf(Lines.get(line_num).split("\\s+")[i])));
					}
				}
			}
			
			//Оператор вывода - println
			else if(Lines.get(line_num).split("\\s+")[0].equals("println")){
				if(Lines.get(line_num).split("\\s+").length<1){
					System.out.println("ParameterError: Too few parameters. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				for(int i=1;i<Lines.get(line_num).split("\\s+").length;i++){	
					//Проверить существует ли переменная
					if(!String_Names.contains(Lines.get(line_num).split("\\s+")[i])&&!Int_Names.contains(Lines.get(line_num).split("\\s+")[i])&&
					!Real_Names.contains(Lines.get(line_num).split("\\s+")[i])&&!Bool_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						System.out.println("NameError: Variable does not exist - \'"+Lines.get(line_num).split("\\s+")[i]
							+"\'. Line "+Integer.toString(line_num+1));
						System.exit(1);
					}
					
					if(String_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						System.out.print(String_Variables.get(String_Names.indexOf(Lines.get(line_num).split("\\s+")[i])));
					}
					else if(Int_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						System.out.print(Int_Variables.get(Int_Names.indexOf(Lines.get(line_num).split("\\s+")[i])));
					}
					else if(Real_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						System.out.print(Real_Variables.get(Real_Names.indexOf(Lines.get(line_num).split("\\s+")[i])));
					}
					else if(Bool_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						System.out.print(Bool_Variables.get(Bool_Names.indexOf(Lines.get(line_num).split("\\s+")[i])));
					}
				}
				System.out.println();
			}
			
			
			
			//Оператор ввода - scan
			else if(Lines.get(line_num).split("\\s+")[0].equals("scan")){
				Scanner in = new Scanner(System.in);
				
				//scan без параметров - задержка до нажатия Enter
				if(Lines.get(line_num).split("\\s+").length==1){
					in.nextLine();
					continue;
				}
				
				//Обработка ввода для каждого типа
				for(int i=1;i<Lines.get(line_num).split("\\s+").length;i++){
					//Проверить существует ли переменная
					if(!String_Names.contains(Lines.get(line_num).split("\\s+")[i])&&!Int_Names.contains(Lines.get(line_num).split("\\s+")[i])&&
					!Real_Names.contains(Lines.get(line_num).split("\\s+")[i])&&!Bool_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						System.out.println("NameError: Variable does not exist - \'"+Lines.get(line_num).split("\\s+")[1]
							+"\'. Line "+Integer.toString(line_num+1));
						System.exit(1);
					}
					if(String_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						String input_str = in.nextLine();
						String_Variables.set(String_Names.indexOf(Lines.get(line_num).split("\\s+")[i]),input_str);
					}
					else if(Int_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						try{
							int input_int = in.nextInt();
							Int_Variables.set(Int_Names.indexOf(Lines.get(line_num).split("\\s+")[i]),input_int);
						}catch(Exception e){
							System.out.println("ScanError: The input data type does not match int"
							+". Line "+Integer.toString(line_num+1));
							System.exit(1);
						}
					}
					else if(Real_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						try{
							double input_real = in.nextDouble();
							Real_Variables.set(Real_Names.indexOf(Lines.get(line_num).split("\\s+")[i]),input_real);
						}catch(Exception e){
							System.out.println("ScanError: The input data type does not match real"
							+". Line "+Integer.toString(line_num+1));
							System.exit(1);
						}
					}
					else if(Bool_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						String input_str = in.nextLine();
						Bool_Variables.set(Bool_Names.indexOf(Lines.get(line_num).split("\\s+")[i]),Aist_ParseBool(input_str));
					}
				}
			}
			
			//Удалить переменную из памяти - destroy
			else if(Lines.get(line_num).split("\\s+")[0].equals("destroy")){
				if(Lines.get(line_num).split("\\s+").length<1){
					System.out.println("ParameterError: Too few parameters. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				
				for(int i=1;i<Lines.get(line_num).split("\\s+").length;i++){
					//Проверить существует ли переменная
					if(!String_Names.contains(Lines.get(line_num).split("\\s+")[i])&&!Int_Names.contains(Lines.get(line_num).split("\\s+")[i])&&
					!Real_Names.contains(Lines.get(line_num).split("\\s+")[i])&&!Bool_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						System.out.println("NameError: Variable does not exist - \'"+Lines.get(line_num).split("\\s+")[i]
							+"\'. Line "+Integer.toString(line_num+1));
						System.exit(1);
					}
					if(String_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						int index_Of = String_Names.indexOf(Lines.get(line_num).split("\\s+")[i]);
						String_Names.remove(index_Of);
						String_Variables.remove(index_Of);
					}
					else if(Int_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						int index_Of = Int_Names.indexOf(Lines.get(line_num).split("\\s+")[i]);
						Int_Names.remove(index_Of);
						Int_Variables.remove(index_Of);
					}
					else if(Real_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						int index_Of = Real_Names.indexOf(Lines.get(line_num).split("\\s+")[i]);
						Real_Names.remove(index_Of);
						Real_Variables.remove(index_Of);
					}
					else if(Bool_Names.contains(Lines.get(line_num).split("\\s+")[i])){
						int index_Of = Bool_Names.indexOf(Lines.get(line_num).split("\\s+")[i]);
						Bool_Names.remove(index_Of);
						Bool_Variables.remove(index_Of);
					}
				}
			}
			
			//======================================================Функция ADD======================
			else if(Lines.get(line_num).split("\\s+")[0].equals("add")||Lines.get(line_num).split("\\s+")[0].equals("+")){
				if(!Lines.get(line_num).contains(":")){
					System.out.println("SyntaxError: The symbol ':' must be in ADD. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				String First_Part = Lines.get(line_num).split(":")[0];
				String Second_Part = Lines.get(line_num).split(":")[1];
				if(First_Part.split("\\s+").length!=2){
					System.out.println("SyntaxError: The add function should return a value of some single variable. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				
					
				if(Second_Part.split("\\s+").length<2){
					System.out.println("ParameterError: The function must take at least two variables. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Существует ли переменная
				if(!String_Names.contains(First_Part.split("\\s+")[1])&&!Int_Names.contains(First_Part.split("\\s+")[1])&&
				!Real_Names.contains(First_Part.split("\\s+")[1])&&!Bool_Names.contains(First_Part.split("\\s+")[1])){
					System.out.println("NameError: Variable does not exist - \'"+First_Part.split("\\s+")[1]
						+"\'. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Является ли она булевой
				if(Bool_Names.contains(First_Part.split("\\s+")[1])){
					System.out.println("TypeError: The ADD operation is not applicable to Boolean variables. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				String All_Type = "";
				if(String_Names.contains(First_Part.split("\\s+")[1])){
					All_Type = "str";
				}else if(Int_Names.contains(First_Part.split("\\s+")[1])){
					All_Type = "int";
				}else if(Real_Names.contains(First_Part.split("\\s+")[1])){
					All_Type = "real";
				}
				String String_Result = "";
				int Int_Result = 0;
				double Real_Result = 0.0;
				for(int i=0;i<Second_Part.split("\\s+").length;i++){
					String Cur_Type = "";
					if(String_Names.contains(Second_Part.split("\\s+")[i])){
						Cur_Type = "str";
					}else if(Int_Names.contains(Second_Part.split("\\s+")[i])){
						Cur_Type = "int";
					}else if(Real_Names.contains(Second_Part.split("\\s+")[i])){
						Cur_Type = "real";
					}else{
						if(Bool_Names.contains(Second_Part.split("\\s+")[i])){
							System.out.println("TypeError: The ADD operation is not applicable to Boolean variables. Line "+Integer.toString(line_num+1));
							System.exit(1);
						}else{
							System.out.println("NameError: Variable does not exist - \'"+Second_Part.split("\\s+")[i]
							+"\'. Line "+Integer.toString(line_num+1));
							System.exit(1);
						}
					}
					if(!All_Type.equals(Cur_Type)){
						System.out.println("TypeError: Incompatible data types. Line "+Integer.toString(line_num+1));
						System.exit(1);
					}
					if(All_Type.equals("str")){
						String_Result += String_Variables.get(String_Names.indexOf(Second_Part.split("\\s+")[i]));
					}else if(All_Type.equals("int")){
						Int_Result += Int_Variables.get(Int_Names.indexOf(Second_Part.split("\\s+")[i]));
					}else if(All_Type.equals("real")){
						Real_Result += Real_Variables.get(Real_Names.indexOf(Second_Part.split("\\s+")[i]));
					}
				}
				if(!First_Part.split("\\s+")[1].equals("nil")){
					if(All_Type.equals("str")){
						String_Variables.set(String_Names.indexOf(First_Part.split("\\s+")[1]),String_Result);
					}else if(All_Type.equals("int")){
						Int_Variables.set(Int_Names.indexOf(First_Part.split("\\s+")[1]),Int_Result);
					}else if(All_Type.equals("real")){
						Real_Variables.set(Real_Names.indexOf(First_Part.split("\\s+")[1]),Real_Result);
					}
				}
			}
			//=======================================================================================
			
			//======================================================Функция sub(-)======================
			else if(Lines.get(line_num).split("\\s+")[0].equals("sub")||Lines.get(line_num).split("\\s+")[0].equals("-")){
				if(!Lines.get(line_num).contains(":")){
					System.out.println("SyntaxError: The symbol ':' must be in SUB. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				String First_Part = Lines.get(line_num).split(":")[0];
				String Second_Part = Lines.get(line_num).split(":")[1];
				if(First_Part.split("\\s+").length!=2){
					System.out.println("SyntaxError: The add function should return a value of some single variable. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				
					
				if(Second_Part.split("\\s+").length<2){
					System.out.println("ParameterError: The function must take at least two variables. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Существует ли переменная
				if(!String_Names.contains(First_Part.split("\\s+")[1])&&!Int_Names.contains(First_Part.split("\\s+")[1])&&
				!Real_Names.contains(First_Part.split("\\s+")[1])&&!Bool_Names.contains(First_Part.split("\\s+")[1])){
					System.out.println("NameError: Variable does not exist - \'"+First_Part.split("\\s+")[1]
						+"\'. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Является ли она булевой
				if(Bool_Names.contains(First_Part.split("\\s+")[1])){
					System.out.println("TypeError: The SUB operation is not applicable to Boolean variables. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Является ли она строковой
				if(String_Names.contains(First_Part.split("\\s+")[1])){
					System.out.println("TypeError: The SUB operation is not applicable to String variables. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				String All_Type = "";
				int Int_Result = 0;
				double Real_Result = 0.0;
				if(Int_Names.contains(First_Part.split("\\s+")[1])){
					All_Type = "int";
					Int_Result = Int_Variables.get(Int_Names.indexOf(Second_Part.split("\\s+")[0]));
				}else if(Real_Names.contains(First_Part.split("\\s+")[1])){
					All_Type = "real";
					Real_Result = Real_Variables.get(Real_Names.indexOf(Second_Part.split("\\s+")[0]));
				}
				
				for(int i=1;i<Second_Part.split("\\s+").length;i++){
					String Cur_Type = "";
					if(Int_Names.contains(Second_Part.split("\\s+")[i])){
						Cur_Type = "int";
					}else if(Real_Names.contains(Second_Part.split("\\s+")[i])){
						Cur_Type = "real";
					}else{
						if(Bool_Names.contains(Second_Part.split("\\s+")[i])){
							System.out.println("TypeError: The SUB operation is not applicable to Boolean variables. Line "+Integer.toString(line_num+1));
							System.exit(1);
						}
						else if(String_Names.contains(Second_Part.split("\\s+")[i])){
							System.out.println("TypeError: The SUB operation is not applicable to String variables. Line "+Integer.toString(line_num+1));
							System.exit(1);
						}else{
							System.out.println("NameError: Variable does not exist - \'"+Second_Part.split("\\s+")[i]
							+"\'. Line "+Integer.toString(line_num+1));
							System.exit(1);
						}
					}
					if(!All_Type.equals(Cur_Type)){
						System.out.println("TypeError: Incompatible data types. Line "+Integer.toString(line_num+1));
						System.exit(1);
					}
					if(All_Type.equals("int")){
						Int_Result -= Int_Variables.get(Int_Names.indexOf(Second_Part.split("\\s+")[i]));
					}else if(All_Type.equals("real")){
						Real_Result -= Real_Variables.get(Real_Names.indexOf(Second_Part.split("\\s+")[i]));
					}
				}
				if(!First_Part.split("\\s+")[1].equals("nil")){
					if(All_Type.equals("int")){
						Int_Variables.set(Int_Names.indexOf(First_Part.split("\\s+")[1]),Int_Result);
					}else if(All_Type.equals("real")){
						Real_Variables.set(Real_Names.indexOf(First_Part.split("\\s+")[1]),Real_Result);
					}
				}
			}
			//=======================================================================================
			//======================================================Функция mul(*)======================
			else if(Lines.get(line_num).split("\\s+")[0].equals("mul")||Lines.get(line_num).split("\\s+")[0].equals("*")){
				if(!Lines.get(line_num).contains(":")){
					System.out.println("SyntaxError: The symbol ':' must be in MUL. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				String First_Part = Lines.get(line_num).split(":")[0];
				String Second_Part = Lines.get(line_num).split(":")[1];
				if(First_Part.split("\\s+").length!=2){
					System.out.println("SyntaxError: The add function should return a value of some single variable. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				
					
				if(Second_Part.split("\\s+").length<2){
					System.out.println("ParameterError: The function must take at least two variables. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Существует ли переменная
				if(!String_Names.contains(First_Part.split("\\s+")[1])&&!Int_Names.contains(First_Part.split("\\s+")[1])&&
				!Real_Names.contains(First_Part.split("\\s+")[1])&&!Bool_Names.contains(First_Part.split("\\s+")[1])){
					System.out.println("NameError: Variable does not exist - \'"+First_Part.split("\\s+")[1]
						+"\'. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Является ли она булевой
				if(Bool_Names.contains(First_Part.split("\\s+")[1])){
					System.out.println("TypeError: The MUL operation is not applicable to Boolean variables. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Является ли она строковой
				if(String_Names.contains(First_Part.split("\\s+")[1])){
					System.out.println("TypeError: The MUL operation is not applicable to String variables. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				String All_Type = "";
				int Int_Result = 0;
				double Real_Result = 0.0;
				if(Int_Names.contains(First_Part.split("\\s+")[1])){
					All_Type = "int";
					Int_Result = Int_Variables.get(Int_Names.indexOf(Second_Part.split("\\s+")[0]));
				}else if(Real_Names.contains(First_Part.split("\\s+")[1])){
					All_Type = "real";
					Real_Result = Real_Variables.get(Real_Names.indexOf(Second_Part.split("\\s+")[0]));
				}
				
				for(int i=1;i<Second_Part.split("\\s+").length;i++){
					String Cur_Type = "";
					if(Int_Names.contains(Second_Part.split("\\s+")[i])){
						Cur_Type = "int";
					}else if(Real_Names.contains(Second_Part.split("\\s+")[i])){
						Cur_Type = "real";
					}else{
						if(Bool_Names.contains(Second_Part.split("\\s+")[i])){
							System.out.println("TypeError: The SUB operation is not applicable to Boolean variables. Line "+Integer.toString(line_num+1));
							System.exit(1);
						}
						else if(String_Names.contains(Second_Part.split("\\s+")[i])){
							System.out.println("TypeError: The SUB operation is not applicable to String variables. Line "+Integer.toString(line_num+1));
							System.exit(1);
						}else{
							System.out.println("NameError: Variable does not exist - \'"+Second_Part.split("\\s+")[i]
							+"\'. Line "+Integer.toString(line_num+1));
							System.exit(1);
						}
					}
					if(!All_Type.equals(Cur_Type)){
						System.out.println("TypeError: Incompatible data types. Line "+Integer.toString(line_num+1));
						System.exit(1);
					}
					if(All_Type.equals("int")){
						Int_Result *= Int_Variables.get(Int_Names.indexOf(Second_Part.split("\\s+")[i]));
					}else if(All_Type.equals("real")){
						Real_Result *= Real_Variables.get(Real_Names.indexOf(Second_Part.split("\\s+")[i]));
					}
				}
				if(!First_Part.split("\\s+")[1].equals("nil")){
					if(All_Type.equals("int")){
						Int_Variables.set(Int_Names.indexOf(First_Part.split("\\s+")[1]),Int_Result);
					}else if(All_Type.equals("real")){
						Real_Variables.set(Real_Names.indexOf(First_Part.split("\\s+")[1]),Real_Result);
					}
				}
			}
			
			
			//======================================================Функция div(/)======================
			else if(Lines.get(line_num).split("\\s+")[0].equals("div")||Lines.get(line_num).split("\\s+")[0].equals("/")){
				if(!Lines.get(line_num).contains(":")){
					System.out.println("SyntaxError: The symbol ':' must be in DIV. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				String First_Part = Lines.get(line_num).split(":")[0];
				String Second_Part = Lines.get(line_num).split(":")[1];
				if(First_Part.split("\\s+").length!=2){
					System.out.println("SyntaxError: The add function should return a value of some single variable. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				
					
				if(Second_Part.split("\\s+").length<2){
					System.out.println("ParameterError: The function must take at least two variables. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Существует ли переменная
				if(!String_Names.contains(First_Part.split("\\s+")[1])&&!Int_Names.contains(First_Part.split("\\s+")[1])&&
				!Real_Names.contains(First_Part.split("\\s+")[1])&&!Bool_Names.contains(First_Part.split("\\s+")[1])){
					System.out.println("NameError: Variable does not exist - \'"+First_Part.split("\\s+")[1]
						+"\'. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Является ли она булевой
				if(Bool_Names.contains(First_Part.split("\\s+")[1])){
					System.out.println("TypeError: The DIV operation is not applicable to Boolean variables. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Является ли она строковой
				if(String_Names.contains(First_Part.split("\\s+")[1])){
					System.out.println("TypeError: The DIV operation is not applicable to String variables. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				String All_Type = "";
				int Int_Result = 0;
				double Real_Result = 0.0;
				if(Int_Names.contains(First_Part.split("\\s+")[1])){
					All_Type = "int";
					Int_Result = Int_Variables.get(Int_Names.indexOf(Second_Part.split("\\s+")[0]));
				}else if(Real_Names.contains(First_Part.split("\\s+")[1])){
					All_Type = "real";
					Real_Result = Real_Variables.get(Real_Names.indexOf(Second_Part.split("\\s+")[0]));
				}
				
				for(int i=1;i<Second_Part.split("\\s+").length;i++){
					String Cur_Type = "";
					if(Int_Names.contains(Second_Part.split("\\s+")[i])){
						Cur_Type = "int";
					}else if(Real_Names.contains(Second_Part.split("\\s+")[i])){
						Cur_Type = "real";
					}else{
						if(Bool_Names.contains(Second_Part.split("\\s+")[i])){
							System.out.println("TypeError: The DIV operation is not applicable to Boolean variables. Line "+Integer.toString(line_num+1));
							System.exit(1);
						}
						else if(String_Names.contains(Second_Part.split("\\s+")[i])){
							System.out.println("TypeError: The DIV operation is not applicable to String variables. Line "+Integer.toString(line_num+1));
							System.exit(1);
						}else{
							System.out.println("NameError: Variable does not exist - \'"+Second_Part.split("\\s+")[i]
							+"\'. Line "+Integer.toString(line_num+1));
							System.exit(1);
						}
					}
					if(!All_Type.equals(Cur_Type)){
						System.out.println("TypeError: Incompatible data types. Line "+Integer.toString(line_num+1));
						System.exit(1);
					}
					if(All_Type.equals("int")){
						Int_Result /= Int_Variables.get(Int_Names.indexOf(Second_Part.split("\\s+")[i]));
					}else if(All_Type.equals("real")){
						Real_Result /= Real_Variables.get(Real_Names.indexOf(Second_Part.split("\\s+")[i]));
					}
				}
				if(!First_Part.split("\\s+")[1].equals("nil")){
					if(All_Type.equals("int")){
						Int_Variables.set(Int_Names.indexOf(First_Part.split("\\s+")[1]),Int_Result);
					}else if(All_Type.equals("real")){
						Real_Variables.set(Real_Names.indexOf(First_Part.split("\\s+")[1]),Real_Result);
					}
				}
			}
			
			//==============================mod============================================
			else if(Lines.get(line_num).split("\\s+")[0].equals("mod")||Lines.get(line_num).split("\\s+")[0].equals("%")){
				if(!Lines.get(line_num).contains(":")){
					System.out.println("SyntaxError: The symbol ':' must be in MOD. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				String First_Part = Lines.get(line_num).split(":")[0];
				String Second_Part = Lines.get(line_num).split(":")[1];
				if(First_Part.split("\\s+").length!=2){
					System.out.println("SyntaxError: The add function should return a value of some single variable. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				
					
				if(Second_Part.split("\\s+").length<2){
					System.out.println("ParameterError: The function must take at least two variables. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Существует ли переменная
				if(!String_Names.contains(First_Part.split("\\s+")[1])&&!Int_Names.contains(First_Part.split("\\s+")[1])&&
				!Real_Names.contains(First_Part.split("\\s+")[1])&&!Bool_Names.contains(First_Part.split("\\s+")[1])){
					System.out.println("NameError: Variable does not exist - \'"+First_Part.split("\\s+")[1]
						+"\'. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Является ли она булевой
				if(Bool_Names.contains(First_Part.split("\\s+")[1])){
					System.out.println("TypeError: The MOD operation is not applicable to Boolean variables. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				//Является ли она строковой
				if(String_Names.contains(First_Part.split("\\s+")[1])){
					System.out.println("TypeError: The MOD operation is not applicable to String variables. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				if(Second_Part.split("\\s+").length!=2){
					System.out.println("ParameterError: The MOD operation takes only two parameters. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				String All_Type = "";
				int Int_Result = 0;
				double Real_Result = 0.0;
				if(Int_Names.contains(First_Part.split("\\s+")[1])){
					All_Type = "int";
					Int_Result = Int_Variables.get(Int_Names.indexOf(Second_Part.split("\\s+")[0]));
				}else if(Real_Names.contains(First_Part.split("\\s+")[1])){
					All_Type = "real";
					Real_Result = Real_Variables.get(Real_Names.indexOf(Second_Part.split("\\s+")[0]));
				}
				
					String Cur_Type = "";
					if(Int_Names.contains(Second_Part.split("\\s+")[1])){
						Cur_Type = "int";
					}else if(Real_Names.contains(Second_Part.split("\\s+")[1])){
						Cur_Type = "real";
					}else{
						if(Bool_Names.contains(Second_Part.split("\\s+")[1])){
							System.out.println("TypeError: The DIV operation is not applicable to Boolean variables. Line "+Integer.toString(line_num+1));
							System.exit(1);
						}
						else if(String_Names.contains(Second_Part.split("\\s+")[1])){
							System.out.println("TypeError: The DIV operation is not applicable to String variables. Line "+Integer.toString(line_num+1));
							System.exit(1);
						}else{
							System.out.println("NameError: Variable does not exist - \'"+Second_Part.split("\\s+")[1]
							+"\'. Line "+Integer.toString(line_num+1));
							System.exit(1);
						}
					}
					if(!All_Type.equals(Cur_Type)){
						System.out.println("TypeError: Incompatible data types. Line "+Integer.toString(line_num+1));
						System.exit(1);
					}
					if(All_Type.equals("int")){
						Int_Result %= Int_Variables.get(Int_Names.indexOf(Second_Part.split("\\s+")[1]));
					}else if(All_Type.equals("real")){
						Real_Result %= Real_Variables.get(Real_Names.indexOf(Second_Part.split("\\s+")[1]));
					}
				if(!First_Part.split("\\s+")[1].equals("nil")){
					if(All_Type.equals("int")){
						Int_Variables.set(Int_Names.indexOf(First_Part.split("\\s+")[1]),Int_Result);
					}else if(All_Type.equals("real")){
						Real_Variables.set(Real_Names.indexOf(First_Part.split("\\s+")[1]),Real_Result);
					}
				}
			}
			//=======================================================================================Mod
			//================GOTO===================
			else if(Lines.get(line_num).split("\\s+")[0].equals("goto")){
				if(Lines.get(line_num).split("\\s+").length!=2){
					System.out.println("ParameterError: The GOTO operation takes only one parameter. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
				String Goto_Label = Lines.get(line_num).split("\\s+")[1];
				int Goto_Place = Goto_Label_Places.get(Goto_Label_Names.indexOf(Goto_Label));
				if(!Goto_Label_Names.contains(Goto_Label)){
					System.out.println("LabelError: Label does not exists - \'"+Goto_Label+"Line "+Integer.toString(line_num));
					System.exit(1);
				}
				line_num = Goto_Place-1;
			}
			//=======================================
			//Если имя функции неправильное, то...
			else{
				Pattern p = Pattern.compile("^-.+$");
				Matcher m = p.matcher(Lines.get(line_num).trim());
				//Если строка - goto метка, то...
				if(!m.matches()){
					System.out.println("SyntaxError: Unknown function - \'"+Lines.get(line_num).split("\\s+")[0]+"\'. Line "+Integer.toString(line_num+1));
					System.exit(1);
				}
			}
			//==============================
			
			
			//Конец цикла
			line_num++;
		}
    }
}