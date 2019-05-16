package cn.ltian.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @ClassName: ExcleUtil
 * @Description: 导出excle通用工具类
 * @author ltian
 * @date 2018 
 *
 * @param <T>
 */
public class ExcelUtil<T> {

	private String sheetName = "sheet1";
	// 列标题
	private String[][] headers = {};
	//标题
	private String titleName = null;
	// 数据集
	private Collection<T> datasets;
	// 日期格式
	private String dateFormat = "yyyy-MM-dd HH:mm:ss";

	public ExcelUtil() {
		super();
	}

	public ExcelUtil(Collection<T> datasets) {
		super();
		this.datasets = datasets;
	}

	public ExcelUtil(String sheetName, Collection<T> datasets) {
		super();
		this.sheetName = sheetName;
		this.datasets = datasets;
	}

	public ExcelUtil(String titleName,String[][] headers, Collection<T> datasets) {
		super();
		this.titleName = titleName;
		this.headers = headers;
		this.datasets = datasets;
	}

	public ExcelUtil(String titleName,String sheetName, String[][] headers, Collection<T> datasets) {
		super();
		this.titleName = titleName;
		this.sheetName = sheetName;
		this.headers = headers;
		this.datasets = datasets;
	}

	public ExcelUtil(String titleName,String sheetName, String[][] headers, Collection<T> datasets, String dateFormat) {
		super();
		this.titleName = titleName;
		this.sheetName = sheetName;
		this.headers = headers;
		this.datasets = datasets;
		this.dateFormat = dateFormat;
	}

	/**
	 *
	 * @Title: exportExcle
	 * @Description: 导出excle
	 * @return    设定文件
	 * @throws
	 */
	public HSSFWorkbook exportExcle() throws Exception {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(sheetName);
		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 标题样式
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		// 设置水平居中
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直居中
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 设置边框
		titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 标题字体
		HSSFFont titleFont = workbook.createFont();
		titleFont.setFontName("微软雅黑");
		titleFont.setColor(HSSFColor.BLACK.index);
		titleFont.setFontHeightInPoints((short) 12);
		// 字体加粗
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		titleStyle.setFont(titleFont);
		// 正文样式
		HSSFCellStyle bodyStyle = workbook.createCellStyle();
		bodyStyle.cloneStyleFrom(titleStyle);
		// 正文字体
		HSSFFont bodyFont = workbook.createFont();
		bodyFont.setFontName("宋体");
		bodyFont.setColor(HSSFColor.BLACK.index);
		bodyFont.setFontHeightInPoints((short) 12);
		bodyStyle.setFont(bodyFont);

		// 表头
		int index = 0;
		HSSFRow row = sheet.createRow(index);
		if (headers.length > 0) {
			if(titleName != null) {
				//设置标题
				HSSFCell titlecell =row.createCell(0);
				// 合并单元格
				sheet.addMergedRegion(new CellRangeAddress(index, index, 0, headers.length-1));
				titlecell.setCellValue(titleName);
				titlecell.setCellStyle(titleStyle);
				//设置标题边框
				HSSFCell titlelastcell =row.createCell(headers.length-1);
				titlelastcell.setCellStyle(titleStyle);
				
				// 设置行高
				row.setHeightInPoints(ExcelConstant.TITLE_HEIGHT_IN_POINTS);
				index++;
				//设置标题结束
				row = sheet.createRow(index);
			}
			// 设置行高
			row.setHeightInPoints(ExcelConstant.HEIGHT_IN_POINTS);
			for (int i = 0; i < headers.length; i++) {
				HSSFCell cell = row.createCell(i);
				HSSFRichTextString text = new HSSFRichTextString(headers[i][0]);
				cell.setCellValue(text);
				cell.setCellStyle(titleStyle);
				// 设置列宽
				sheet.setColumnWidth(i, Integer.parseInt(headers[i][1]) * 256);
			}
			
		}

		// 遍历集合数据，产生数据行
		Iterator<T> it = datasets.iterator();
		while (it.hasNext()) {
			++index;
			row = sheet.createRow(index);
			// 设置行高
			row.setHeightInPoints(ExcelConstant.HEIGHT_IN_POINTS);
			T t = (T) it.next();
			// 利用反射，得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(bodyStyle);
				Field field = fields[i];
				field.setAccessible(true);
				Object value = field.get(t);
				// 判断值的类型后进行强制类型转换
				String textValue = null;
				if (value instanceof Date) {
					Date date = (Date) value;
					SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
					textValue = sdf.format(date);
				} else if (value instanceof byte[]) {
					// 设置图片行行高
					row.setHeightInPoints(ExcelConstant.PICTURE_HEIGHT);
					byte[] bsValue = (byte[]) value;
					HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) i, index, (short) i, index);
					anchor.setAnchorType(2);
					patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
				} else {
					// 其它数据类型都当作字符串简单处理
					//数据如果是null 置空
					if(value == null) {
						value = "";
					}
					textValue = value.toString();
				}
				// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
				if (textValue != null) {
					Pattern p = Pattern.compile("^//d+(//.//d+)?$");
					Matcher matcher = p.matcher(textValue);
					if (matcher.matches()) {
						// 是数字当作double处理,保留一位小数
						NumberFormat format = new DecimalFormat("0.0");
						cell.setCellValue(Double.parseDouble(format.format(textValue)));
					} else {
						cell.setCellValue(textValue);
					}
				}
			}
		}

		return workbook;
	}

}