package utillity;

public class FinalVariables {
	//JOptionPane messages
	public final String CLOSE_WINDOW = "Czy napewno chcesz zamknąć te okienko?";
	public final String WINDOW_ERROR = "Coś poszło nie tak\n";
	public final String PRINTER_CHANGE = "Czy napewno chcesz zmienić drukarkę? Może to spowodować problemy z drukowaniem.";
	
	//paths - consider to allow to specify this by user
	public final String ICON_PATH = "resources\\img\\icon_hct.png";//D:\\@Development\\EclipseJavaProjects\\sqliteTestApp\\StockApp\\
	public final String INVOICE_LOGO_PATH = "resources\\img\\Logo HCT 245x84.png";//D:\\@Development\\EclipseJavaProjects\\sqliteTestApp\\StockApp\\
	public final String DATABASE_DEFAULT_PATH = "SQLite\\theDBase.sqlite";
//	public final String DATABASE_DEFAULT_PATH = "D:\\@Development\\__TEMP\\SQLite\\theDBase.sqlite";
	public final String DATABASE_BACKUP_DEFAULT_PATH = "C:\\Users\\hctba\\OneDrive\\HCT - Dokumenty\\BackUpy\\database";
	public String PRINTER_NAME = "Canon MP620 series Printer WS";
	public String SAVE_FOLDER_DEFAULT_PATH = "D:\\@Development\\__TEMP";
	public  final String LOGGER_FOLDER_NAME = "_LOG_FILES";
	
	//patterns and formats
	public final String DECIMAL_PATTERN = "^-?([0-9]*)\\.([0-9]*)+$";	
	public final String INTEGER_PATTERN = "^-?\\d+$";
	public final String DECIMAL_FORMAT = "#.##";
	public final String DATE_FORMAT = "dd-MM-yyyy";//"yyyy-MM-dd" ?? not sure which format is correct for a database entry. so far works

	//other
	public final String SEARCH_TEXT_FIELD_FRAZE = "wpisz szukaną nazwę";
	public final String OTHER_STRING_CHECKUP = "O:";

	
	//Database related
	public final String DELETING_ERROR = "Usuwanie rekordu z bazy danych nie powiodło się";
	public final String INSERT_SUCCESS = "Zapisano pomyślnie";
	public final String EDIT_SUCCESS = "Edycja zakończona pomyślnie";
	public final String DELETE_SUCCESS = "Usuwanie zakończone pomyślnie";
	//Database table names
	public final String INVOCE_TABLE = "invoice_list";
	public final String MANUFACTURER_LIST_TABLE = "manufacturers";
	public final String SERVICES_TABLE = "services";
	public final String STOCK_TABLE = "stock";
	public final String USERS_TABLE = "users";
	public final String SETTINGS_TABLE = "settings";
	//Database columns names 
	public final String SERVICE_TABLE_NUMBER = "service_number";
	public final String SERVICES_TABLE_SERVICE_NAME = "service_name";
	public final String STOCK_TABLE_NUMBER = "stock_number";
	public final String STOCK_TABLE_ITEM_NAME = "item_name";
	public final String STOCK_TABLE_QNT = "quantity";
	public final String MANUFACTURER_TABLE_NAME = "manufacturer";
	public final String STOCK_TABLE_PRICE = "price";
	public final String INVOCE_TABLE_INVOICE_NUMBER = "invoice_number";
	public final String INVOCE_TABLE_CUSTOMER_NAME = "customer_name";
	public final String INVOCE_TABLE_DATE = "invoice_date";
	public final String SETTINGS_TABLE_PATH = "path";
	public final String ROW_ID = "rowid";
	
	public final int DEFAULT_FOLDER_DATABASE_ROW_ID = 1;
	public final int PRINTER_DATABASE_ROW_ID = 2;
	public final int SETTINGS_TABLE_LAST_DATABASE_BACKUP = 3;
	
	public final int DEFAULT_FOLDER_ARRAYLIST_INDEX = 0;
	public final int PRINTER__ARRAYLIST_INDEX = 1;

}
