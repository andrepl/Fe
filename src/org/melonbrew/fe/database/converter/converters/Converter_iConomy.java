package org.melonbrew.fe.database.converter.converters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.melonbrew.fe.Fe;
import org.melonbrew.fe.SQLibrary.Database;
import org.melonbrew.fe.database.converter.Converter;
import org.melonbrew.fe.database.databases.SQLDB;

public class Converter_iConomy extends Converter {
	public String getName(){
		return "iConomy";
	}
	
	public boolean isFlatFile(){
		return true;
	}
	
	public boolean isMySQL(){
		return true;
	}
	
	public boolean convertFlatFile(Fe plugin){
		File accountsFile = new File("plugins/iConomy/accounts.mini");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(accountsFile));
			
			String line = null;
			
			while ((line = reader.readLine()) != null){
				line = line.replace("balance:", "");
				
				String[] args = line.split(" ");
				
				String name = args[0];
				
				double money = Double.parseDouble(args[1]);
				
				plugin.getAPI().createAccount(name).setMoney(money);
			}
			
			reader.close();
		} catch (Exception e){
			return false;
		}
		
		return true;
	}
	
	public boolean convertMySQL(Fe plugin){
		Database database = ((SQLDB) plugin.getFeDatabase()).getDatabase();
		
		try {
			database.query("ALTER TABLE iconomy DROP COLUMN id;");
			database.query("ALTER TABLE iconomy DROP COLUMN status;");
			database.query("ALTER TABLE iconomy CHANGE username name varchar(64);");
			database.query("ALTER TABLE iconomy CHANGE balance money double;");

			database.query("RENAME TABLE iconomy TO fe_accounts;");
		}catch (Exception e){
			return false;
		}
		
		return true;
	}
}
