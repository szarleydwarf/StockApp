package utillity;

import javax.swing.ComboBoxModel;

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
	public final String DATABASE_BACKUP_DEFAULT_PATH = "C:\\Users\\hctba\\OneDrive\\HCT - Dokumenty\\BackUpy\\database";//C:\Users\hctba\OneDrive\HCT - Dokumenty\BackUpy\database
	public String PRINTER_NAME = "Canon MP620 series Printer WS";
	public String SAVE_FOLDER_DEFAULT_PATH = "D:\\@Development\\__TEMP";
	public  final String LOGGER_FOLDER_NAME = "_LOG_FILES";
	
	//patterns and formats
	public final String DECIMAL_PATTERN = "^-?([0-9]*)\\.([0-9]*)+$";	
	public final String INTEGER_PATTERN = "^-?\\d+$";
	public final String DECIMAL_FORMAT = "000.00";
	public static final String DECIMAL_FORMAT_5_2 = "00000.00";
	public final String DATE_FORMAT = "dd-MM-yyyy";//"yyyy-MM-dd" ?? not sure which format is correct for a database entry. so far works
	public final String DATE_TIME_FORMAT = "dd-MM-yyyy @HH:mm:ss";

	//other
	public final String SEARCH_TEXT_FIELD_FRAZE = "wpisz szukaną nazwę";
	public final String OTHER_STRING_CHECKUP = "O:";
	public final String COMPANY_STRING = "C:";
	public final String MAX_SERVIS_QNT = "9999";
	public final String[] SORT_BY = {"Nazwa", "Cena", "Qnt"};//, "Rozmiar"
	public final String WASH = "Wash";
	public final String STAR = "*";

	public final String STOCK_SORT_BY="item_name";
	public final String SERVICES_SORT_BY="service_name";
	public final String AAA = "AAA";
	public final String AAS = "AAS";
	
	public final String[] DAYS_NUM_31 = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
	public final String[] DAYS_NUM_30 = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"};
	public final String[] DAYS_NUM_29 = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29"};
	public final String[] DAYS_NUM_28 = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28"};
	public final String[] YEARS_NO_STRING = {"2017", "2018", "2019", "2020"};
	public final String[] SALES_REPORT_ROW_HEADINGS = {"Tyre Sale", "Tyre Serv", "Car Wash ", "Discounts", "*** TOTAL"};
	public final String[] SALES_REPORT_COL_HEADINGS = {"SALE", "COST", "PROFIT"};
	public static final float PAGE_MARGIN = 20;

	public final long TIMEOUT = 50000;

	
	//Database related
	public final String DELETING_ERROR = "Usuwanie rekordu z bazy danych nie powiodło się";
	public final String INSERT_SUCCESS = "Zapisano pomyślnie";
	public final String SAVE_ERROR = "Zapis dokumentu nie powiódł się. Sprawdż LOG";
	public final String PRINT_ERROR = "Wydruk  dokumentu nie powiódł się. Sprawdż LOG";
	public final String EDIT_SUCCESS = "Edycja zakończona pomyślnie";
	public final String DELETE_SUCCESS = "Usuwanie zakończone pomyślnie";

	//Other tables
//	public static final String[] MONTHS_2017 = {"01-2017", "02-2017", "03-2017", "04-2017", "05-2017", "06-2017", "07-2017", "08-2017", "09-2017", "10-2017", "11-2017", "12-2017"};
	public static final int MONTHS_NUM = 12;
	public static final String[] MONTHS_NAMES = {"Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"};
	public static final String[] SALES_REPORT_TB_HEADINGS = {"Miesiąc", "Koszty", "Przychód", "Zysk"};
	public static final String[] STOCK_TB_HEADINGS = {"Towar", "Koszt", "Cena", "Qnt"};
	public static final String[] STOCK_TB_HEADINGS_NO_COST =  {"Towar", "Cena", "Qnt"};
	public static final String[] CARS_TABLE_HEADER = {"Marka auta"};
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

=======
=======
	public static final String[] INVOICE_REPORT_TB_HEADINGS = {"Inv #", "Klient", "Usługi", "Produkty", "Total", "Data faktury", "Ścieżka dostępu"};
>>>>>>> try_item_class
=======
	public static final String[] INVOICE_REPORT_TB_HEADINGS = {"Inv #", "Klient", "Usługi", "Produkty", "Total", "Data faktury", "Ścieżka dostępu", "Discount"};
>>>>>>> try_item_class
	public final String CARS_TB_NAME = "CARS";
	public final String STOCK_TB_NAME = "STOCK";
	public final String CHOSEN_TB_NAME = "CHOOSEN";
	public static final String CUSTOMER_TABLE = "customers";
	public static final String CUSTOMER_ID = "id";
	public static final String[] CUSTOMER_TB_HEADINGS = {"ID", "CAR", "REGISTRATION", "DETAILS", "BUSINESS", "# VISITS"};
	public static final String CUSTOMER_QUERY = "SELECT * from "+CUSTOMER_TABLE+" ORDER BY "+CUSTOMER_ID+" ASC";

	//Database table names
>>>>>>> try_item_class
	public static final String TOTAL = "total";
	public static final String COST_COL_NAME = "cost";
	public final String PRICE_COL_NAME = "price";
	public final String INVOCE_TABLE = "invoice_list";
	public final String MANUFACTURER_TABLE = "manufacturers";
	public final String MANUFACTURER_TABLE_CAR_ID = "car_id";
	public final String MANUFACTURER_TABLE_BRAND = "manufacturer";
	
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
	public final String INVOCE_TABLE_INVOICE_NUMBER = "invoice_number";
	public final String INVOCE_TABLE_CUSTOMER_NAME = "customer_name";
	public final String INVOCE_TABLE_DATE = "invoice_date";
	public final String SETTINGS_TABLE_PATH = "path";
	public final String ROW_ID = "rowid";
	
	public final int DEFAULT_FOLDER_DATABASE_ROW_ID = 1;
	public final int PRINTER_DATABASE_ROW_ID = 2;
	public final int SETTINGS_TABLE_LAST_DATABASE_BACKUP = 3;
	public final int DATABASE_BACKUP_PATH_ROW_ID = 4;
	
	public final int DEFAULT_FOLDER_ARRAYLIST_INDEX = 0;
	public final int PRINTER__ARRAYLIST_INDEX = 1;
	public int FREEBIES_ARRAY_SIZE = 4;
	public static final String[] FREEBIES_ARRAY = {"Air freshener", "Tyre paint", "Valve caps", "Free Car Wash"};
}
