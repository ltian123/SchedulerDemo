package cn.ltian.excel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 读取excle 接口
 * @author ltian
 *
 */
public interface ReadExcelUtilInterface {
//	创建execl2007
	public void create2007Excel()  throws FileNotFoundException,IOException ;
//	创建execl2003
	public void create2003Excel() throws FileNotFoundException,IOException;
//	读取execl，自动判断版本（根据后缀）
	public Map<String,List<Object>> readExcel(File file, String[] tables) throws Exception;
//	读取execl2007
	public Map<String,List<Object>> read2007Excel(File file, String[] tables) throws Exception;
//	读取execl2003
	public Map<String,List<Object>> read2003Excel(File file, String[] tables) throws Exception;
//	读取execl，自动判断版本（根据后缀）FirstRowNum 
	/**
	 * 
	 * @param file 文件
	 * @param FirstRowNum  从第几行开始读取 
	 * @param LastCellNum  一共读取的列数
	 * @return
	 * @throws Exception
	 */
	public List<List<Object>>  readExcel(File file, Integer FirstRowNum, Integer LastCellNum) throws Exception;
//	读取execl2007
	public List<List<Object>>  read2007Excel(File file) throws Exception;
//	读取execl2003
	/**
	 * 
	 * @param file 文件
	 * @param FirstRowNum  从第几行开始读取 
	 * @param LastCellNum  一共读取的列数
	 * @return
	 * @throws Exception
	 */
	public List<List<Object>>  read2003Excel(File file, Integer FirstRowNum, Integer LastCellNum) throws Exception;
	/**
	 * 
	* @Title: export03   
	* @Description: 报表导出2003
	* @param @param listSalesdetails
	* @param @return
	* @param @throws Exception      
	* @return HSSFWorkbook   
	* @author zhaojie
	* @date 2014-3-28 下午12:58:35 
	* @throws
	 */
	public void export07() throws FileNotFoundException,IOException;
}
