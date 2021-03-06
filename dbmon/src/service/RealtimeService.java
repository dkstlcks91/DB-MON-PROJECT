package service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import org.apache.log4j.Logger;

import controller.RealtimeController;
import dao.RealtimeMonDao;
import dto.PerformenceDto;
import dto.TopSqlDto;
import dto.WaitEventDto;

/**
 * 
* 1. 패키지명 : main.service
* 2. 타입명 : RealtimeService.java
* 3. 작성일 : 2015. 8. 20. 오전 10:03:50
* 4. 작성자 : 길용현
* 5. 설명 : Realtime Monitoring TAB Service 클래스
 */
public class RealtimeService {
	private static final Logger LOG = Logger.getLogger(RealtimeService.class);
	
	// performence 항목
	private static XYChart.Series<String, Number> bufferCacheHitSeries;
	public static ObservableList bufferCacheHitList;
    private static XYChart.Series<String, Number> libraryCacheHitSeries;
    public static ObservableList libraryCacheHitList;
    private static XYChart.Series<String, Number> dictionaryCacheHitSeries;
    public static ObservableList dictionaryCacheHitList;
    private static XYChart.Series<String, Number> inMemoryHitSeries;
    public static ObservableList inMemoryHitList;
    
    // wait event 항목
    private static XYChart.Series<String, Number> bufferBusyWaitSeries;
    public static ObservableList bufferBusyWaitList;
    private static XYChart.Series<String, Number> logFileSyncSeries;
    public static ObservableList logFileSyncList;
    private static XYChart.Series<String, Number> dbFileSequentialSeries;
    public static ObservableList dbFileSequentialList;
    private static XYChart.Series<String, Number> dbFileScatteredSeries;
    public static ObservableList dbFileScatteredList;
    private static XYChart.Series<String, Number> libraryCacheLockSeries;
    public static ObservableList libraryCacheLockList;
    private static XYChart.Series<String, Number> logBufferSpaceSeries;
    public static ObservableList logBufferSpaceList;
    
    // TOP3 SQL 항목 
    private static XYChart.Series<String, Number> bufferGetstSeries;
    public static ObservableList bufferGetsList;
    private static XYChart.Series<String, Number> cpuTimeSeries;
    public static ObservableList cpuTimeList;
    private static XYChart.Series<String, Number> elapsedTimelSeries;
    public static ObservableList elapsedTimeList;
    private static XYChart.Series<String, Number> executionsSeries;
    public static ObservableList executionsList;
    
    // JDBC Connection 항목
    private static XYChart.Series<String, Number> jdbcConnectSeries;
    public static ObservableList jdbcConnectList;
    
    private static RealtimeService instance;
    
    // chart initialize 시 사용하는 변수
    private int increasefun = 0;
    private int decreasefun = 0;
    private int c = 0;
    
    // sound 관련 변수
    private static Media media;
	private static MediaPlayer mediaPlayer;
	private static String path = System.getProperty("user.dir")+"/src/sound/warning.mp3";
	
	static {
		media = new Media(new File(path).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
	}
    
	private RealtimeService(){
		
	}
	
	public static RealtimeService getInstance(){
		if(instance == null){
			instance = new RealtimeService();
		}
		
		return instance;
	}
	
	/**
	 * 
	* 1. 메소드명 : chartListSetting
	* 2. 작성일 : 2015. 8. 20. 오전 11:19:22
	* 3. 작성자 : 길용현
	* 4. 설명 : chart 에서 사용하는  Series, list 초기화
	 */
	public void chartListSetting(){
		// performence 항목
		bufferCacheHitSeries = new XYChart.Series<String, Number>();
		bufferCacheHitList = FXCollections
				.observableArrayList(bufferCacheHitSeries);
		libraryCacheHitSeries = new XYChart.Series<String, Number>();
		libraryCacheHitList = FXCollections
				.observableArrayList(libraryCacheHitSeries);
		dictionaryCacheHitSeries = new XYChart.Series<String, Number>();
		dictionaryCacheHitList = FXCollections
				.observableArrayList(dictionaryCacheHitSeries);
		inMemoryHitSeries = new XYChart.Series<String, Number>();
		inMemoryHitList = FXCollections.observableArrayList(inMemoryHitSeries);

		// wait event 항목
		bufferBusyWaitSeries = new XYChart.Series<String, Number>();
		bufferBusyWaitList = FXCollections
				.observableArrayList(bufferBusyWaitSeries);
		logFileSyncSeries = new XYChart.Series<String, Number>();
		logFileSyncList = FXCollections.observableArrayList(logFileSyncSeries);
		dbFileSequentialSeries = new XYChart.Series<String, Number>();
		dbFileSequentialList = FXCollections
				.observableArrayList(dbFileSequentialSeries);
		dbFileScatteredSeries = new XYChart.Series<String, Number>();
		dbFileScatteredList = FXCollections
				.observableArrayList(dbFileScatteredSeries);
		libraryCacheLockSeries = new XYChart.Series<String, Number>();
		libraryCacheLockList = FXCollections
				.observableArrayList(libraryCacheLockSeries);
		logBufferSpaceSeries = new XYChart.Series<String, Number>();
		logBufferSpaceList = FXCollections
				.observableArrayList(logBufferSpaceSeries);

		// TOP3 SQL 항목
		bufferGetstSeries = new XYChart.Series<String, Number>();
		bufferGetsList = FXCollections.observableArrayList(bufferGetstSeries);
		cpuTimeSeries = new XYChart.Series<String, Number>();
		cpuTimeList = FXCollections.observableArrayList(cpuTimeSeries);
		elapsedTimelSeries = new XYChart.Series<String, Number>();
		elapsedTimeList = FXCollections.observableArrayList(elapsedTimelSeries);
		executionsSeries = new XYChart.Series<String, Number>();
		executionsList = FXCollections.observableArrayList(executionsSeries);

		// JDBC Connection 항목
		jdbcConnectSeries = new XYChart.Series<String, Number>();
		jdbcConnectList = FXCollections.observableArrayList(jdbcConnectSeries);
		
		RealtimeMonDao.getInstance().initJdbcMap();
	}
	
	/**
     * 
    * 1. 메소드명 : initChartData
    * 2. 작성일 : 2015. 8. 15. 오후 5:11:50
    * 3. 작성자 : 길용현
    * 4. 설명 : chart 에서 사용하는 Series, list 초기화 - Sub
     */
	public void initChartData(XYChart.Series<String, Number> chartSeries, 
			ObservableList chartList) {
		
		chartSeries = new XYChart.Series<String, Number>();
		chartList = FXCollections.observableArrayList(chartSeries);
	}
	
	/**
	 * 
	* 1. 메소드명 : barChartColorSetting
	* 2. 작성일 : 2015. 8. 17. 오후 4:45:27
	* 3. 작성자 : 길용현
	* 4. 설명 : bar chart color 설정
	* @param data
	* @param color
	 */
	private void barChartColorSetting(XYChart.Data<String,Number> data, String color){
		data.getNode().setStyle("-fx-bar-fill: "+color+";");
	}
	
	/**
	 * 
	* 1. 메소드명 : addData
	* 2. 작성일 : 2015. 8. 13. 오후 10:10:44
	* 3. 작성자 : 길용현
	* 4. 설명 : 각 차트 별 데이터 추가
	 */
	public boolean addData(int cnt){
		// performence 항목 chart data 추가
		ArrayList<PerformenceDto> performenceList = RealtimeMonDao.getInstance().getPerformenceData();
		double bufferCacheValue = ((PerformenceDto)performenceList.get(0)).getValue();
		double libraryCacheValue = ((PerformenceDto)performenceList.get(1)).getValue();
		double dictionaryCacheValue = ((PerformenceDto)performenceList.get(2)).getValue();
		double inMemoryValue = ((PerformenceDto)performenceList.get(3)).getValue();
		
		bufferCacheHitSeries.getData().add(new XYChart.Data<String,Number>(cnt+"", (Number)bufferCacheValue));
		libraryCacheHitSeries.getData().add(new XYChart.Data<String,Number>(cnt+"", (Number)libraryCacheValue));
		dictionaryCacheHitSeries.getData().add(new XYChart.Data<String,Number>(cnt+"", (Number)dictionaryCacheValue));
		inMemoryHitSeries.getData().add(new XYChart.Data<String,Number>(cnt+"", (Number)inMemoryValue));
		
		// wait event 항목 chart data 추가
		ArrayList<WaitEventDto> waitEventList = RealtimeMonDao.getInstance().getWaitEventData();
		double bufferBusyWaitsValue = ((WaitEventDto)waitEventList.get(0)).getAverageWait();
		double logFileSyncValue = ((WaitEventDto)waitEventList.get(1)).getAverageWait();
		double dbFileSequentialReadValue = ((WaitEventDto)waitEventList.get(2)).getAverageWait();
		double dbFileScatteredReadValue = ((WaitEventDto)waitEventList.get(3)).getAverageWait();
		double libraryCacheLockValue = ((WaitEventDto)waitEventList.get(4)).getAverageWait();
		double logBufferSpaceValue = ((WaitEventDto)waitEventList.get(5)).getAverageWait();
		
		bufferBusyWaitSeries.getData().add(new XYChart.Data<String,Number>(cnt+"", (Number)bufferBusyWaitsValue));
	    logFileSyncSeries.getData().add(new XYChart.Data<String,Number>(cnt+"", (Number)logFileSyncValue));	    
	    dbFileSequentialSeries.getData().add(new XYChart.Data<String,Number>(cnt+"", (Number)dbFileSequentialReadValue));	    
	    dbFileScatteredSeries.getData().add(new XYChart.Data<String,Number>(cnt+"", (Number)dbFileScatteredReadValue));	    
	    libraryCacheLockSeries.getData().add(new XYChart.Data<String,Number>(cnt+"", (Number)libraryCacheLockValue));	    
	    logBufferSpaceSeries.getData().add(new XYChart.Data<String,Number>(cnt+"", (Number)logBufferSpaceValue));
	    
	    // TOP3 SQL 항목 chart data 추가
	    ArrayList<TopSqlDto> topSqlList = RealtimeMonDao.getInstance().getTopSqlData();

	    double bufferGetsValue1 = 0;
	    double bufferGetsValue2 = 0;
	    double bufferGetsValue3 = 0;
	    if(topSqlList.size() != 0){
	    	bufferGetsValue1 = ((TopSqlDto)topSqlList.get(0)).getTop1();
	    	bufferGetsValue2 = ((TopSqlDto)topSqlList.get(0)).getTop2();
	    	bufferGetsValue3 = ((TopSqlDto)topSqlList.get(0)).getTop3();
	    }
	    
	    if(cnt==15){
	    	XYChart.Data<String,Number> data1 = new XYChart.Data<String,Number>("1", (Number)bufferGetsValue1);
	    	XYChart.Data<String,Number> data2 = new XYChart.Data<String,Number>("2", (Number)bufferGetsValue2);
	    	XYChart.Data<String,Number> data3 = new XYChart.Data<String,Number>("3", (Number)bufferGetsValue3);
	    	bufferGetstSeries.getData().add(data1);
	    	bufferGetstSeries.getData().add(data2);
	    	bufferGetstSeries.getData().add(data3);
	    	barChartColorSetting(data1, "#DF1E3A");
	    	barChartColorSetting(data2, "#F0C74D");
	    	barChartColorSetting(data3, "#9CC4E4");
	    }else{
	    	bufferGetstSeries.getData().get(0).setYValue((Number)bufferGetsValue1);
	    	bufferGetstSeries.getData().get(1).setYValue((Number)bufferGetsValue2);
	    	bufferGetstSeries.getData().get(2).setYValue((Number)bufferGetsValue3);
	    }

    	double cpuTimeValue1 = 0;
    	double cpuTimeValue2 = 0;
    	double cpuTimeValue3 = 0;
    	if(topSqlList.size() != 0){
    		cpuTimeValue1 = ((TopSqlDto)topSqlList.get(1)).getTop1();
    		cpuTimeValue2 = ((TopSqlDto)topSqlList.get(1)).getTop2();
    		cpuTimeValue3 = ((TopSqlDto)topSqlList.get(1)).getTop3();
    	}
    	
    	if(cnt==15){
    		XYChart.Data<String,Number> data1 = new XYChart.Data<String,Number>("1", (Number)cpuTimeValue1);
	    	XYChart.Data<String,Number> data2 = new XYChart.Data<String,Number>("2", (Number)cpuTimeValue2);
	    	XYChart.Data<String,Number> data3 = new XYChart.Data<String,Number>("3", (Number)cpuTimeValue3);
    		cpuTimeSeries.getData().add(data1);
    	    cpuTimeSeries.getData().add(data2);
    	    cpuTimeSeries.getData().add(data3);
    	    barChartColorSetting(data1, "#DF1E3A");
	    	barChartColorSetting(data2, "#F0C74D");
	    	barChartColorSetting(data3, "#9CC4E4");
    	}else{
    		cpuTimeSeries.getData().get(0).setYValue((Number)cpuTimeValue1);
    		cpuTimeSeries.getData().get(1).setYValue((Number)cpuTimeValue2);
    		cpuTimeSeries.getData().get(2).setYValue((Number)cpuTimeValue3);
    	}
	    
	    double elapsedTimeValue1 = 0;
	    double elapsedTimeValue2 = 0;
	    double elapsedTimeValue3 = 0;
	    if(topSqlList.size() != 0){
	    	elapsedTimeValue1 = ((TopSqlDto)topSqlList.get(2)).getTop1();
	    	elapsedTimeValue2 = ((TopSqlDto)topSqlList.get(2)).getTop2();
	    	elapsedTimeValue3 = ((TopSqlDto)topSqlList.get(2)).getTop3();
	    }
	    
	    if(cnt==15){
	    	XYChart.Data<String,Number> data1 = new XYChart.Data<String,Number>("1", (Number)elapsedTimeValue1);
	    	XYChart.Data<String,Number> data2 = new XYChart.Data<String,Number>("2", (Number)elapsedTimeValue2);
	    	XYChart.Data<String,Number> data3 = new XYChart.Data<String,Number>("3", (Number)elapsedTimeValue3);
	    	elapsedTimelSeries.getData().add(data1);
	    	elapsedTimelSeries.getData().add(data2);
	    	elapsedTimelSeries.getData().add(data3);
	    	barChartColorSetting(data1, "#DF1E3A");
	    	barChartColorSetting(data2, "#F0C74D");
	    	barChartColorSetting(data3, "#9CC4E4");
	    }else{
	    	elapsedTimelSeries.getData().get(0).setYValue((Number)elapsedTimeValue1);
	    	elapsedTimelSeries.getData().get(1).setYValue((Number)elapsedTimeValue2);
	    	elapsedTimelSeries.getData().get(2).setYValue((Number)elapsedTimeValue3);
	    }
	    	    
	    double executionsValue1 = 0;
	    double executionsValue2 = 0;
	    double executionsValue3 = 0;
	    if(topSqlList.size() != 0){
	    	executionsValue1 = ((TopSqlDto)topSqlList.get(3)).getTop1();
	    	executionsValue2 = ((TopSqlDto)topSqlList.get(3)).getTop2();
	    	executionsValue3 = ((TopSqlDto)topSqlList.get(3)).getTop3();
	    }
	    
	    if(cnt==15){
	    	XYChart.Data<String,Number> data1 = new XYChart.Data<String,Number>("1", (Number)executionsValue1);
	    	XYChart.Data<String,Number> data2 = new XYChart.Data<String,Number>("2", (Number)executionsValue2);
	    	XYChart.Data<String,Number> data3 = new XYChart.Data<String,Number>("3", (Number)executionsValue3);
	    	executionsSeries.getData().add(data1);
	    	executionsSeries.getData().add(data2);
	    	executionsSeries.getData().add(data3);
	    	barChartColorSetting(data1, "#DF1E3A");
	    	barChartColorSetting(data2, "#F0C74D");
	    	barChartColorSetting(data3, "#9CC4E4");
	    }else{
	    	executionsSeries.getData().get(0).setYValue((Number)executionsValue1);
	    	executionsSeries.getData().get(1).setYValue((Number)executionsValue2);
	    	executionsSeries.getData().get(2).setYValue((Number)executionsValue3);
	    }
	    
	    if(cnt>=45){
	    	bufferCacheHitSeries.getData().remove(0);
	        libraryCacheHitSeries.getData().remove(0);
	        dictionaryCacheHitSeries.getData().remove(0);
	        inMemoryHitSeries.getData().remove(0);
	        bufferBusyWaitSeries.getData().remove(0);
	        logFileSyncSeries.getData().remove(0);
	        dbFileSequentialSeries.getData().remove(0);
	        dbFileScatteredSeries.getData().remove(0);
	        libraryCacheLockSeries.getData().remove(0);
	        logBufferSpaceSeries.getData().remove(0);
    	}
	    
	    // JDBC Connection data 추가
	    HashMap<Integer, HashSet<String>> jdbcMap = RealtimeMonDao.getInstance().getJdbcConnectData();
	    Set<Integer> set = jdbcMap.keySet();
		
		for(Object obj : set){
			int num = (int)obj;
			
			if(cnt==15){
				XYChart.Data<String,Number> data = new XYChart.Data<String,Number>(obj +"", (Number)jdbcMap.get(obj).size());
		    	jdbcConnectSeries.getData().add(data);
		    	barChartColorSetting(data, "#DF1E3A");
		    }else{
		    	jdbcConnectSeries.getData().get(num).setYValue((Number)jdbcMap.get(obj).size());
		    }
		}
	   
	    // Online Users data 추가
	    int onlineUsersCnt = RealtimeMonDao.getInstance().getOnlineUsersData();
	    int currentUsersCnt = Integer.parseInt(RealtimeController.realtimeController.getOnlineUserLabel().getText());
	    
	    if(currentUsersCnt != onlineUsersCnt){
	    	RealtimeController.realtimeController.setOnlineUserLabel(onlineUsersCnt+"");
	    }
	    
	    LOG.info("");
	    LOG.info("[Performance] - "+bufferCacheValue+" , "+libraryCacheValue+" , "+dictionaryCacheValue+" , "+inMemoryValue);
	    LOG.info("[Wait Event] - "+dbFileScatteredReadValue+" , "+dbFileSequentialReadValue+" , "+logFileSyncValue+
	    		" , "+bufferBusyWaitsValue+" , "+logBufferSpaceValue+" , "+libraryCacheLockValue);
	    LOG.info("[TOP3 SQL] - "+"("+bufferGetsValue1+" , "+bufferGetsValue2+" , "+bufferGetsValue3+") , "
	    		+"("+cpuTimeValue1+" , "+cpuTimeValue2+" , "+cpuTimeValue3+"), "
	    		+"("+elapsedTimeValue1+" , "+elapsedTimeValue2+" , "+elapsedTimeValue3+"), "
	    		+"("+executionsValue1+" , "+executionsValue2+" , "+executionsValue3+")");
	    
	    StringBuffer sb = new StringBuffer();
	    
	    for(Object obj : set){
	    	int key = (int)obj;
	    	sb.append("("+key+":"+jdbcMap.get(obj).size()+") ");
	    }
	    
	    LOG.info("[JDBC Connection] - "+sb.toString());
	    LOG.info("[Online Users] - "+onlineUsersCnt);
	        
	    if(topSqlList.size() != 0){
	    	setTooltipTop3SQL(bufferGetstSeries, topSqlList, 0);
		    setTooltipTop3SQL(cpuTimeSeries, topSqlList, 1);
		    setTooltipTop3SQL(elapsedTimelSeries, topSqlList, 2);
		    setTooltipTop3SQL(executionsSeries, topSqlList, 3);
	    }
	    
	    if(bufferCacheValue < 90 || libraryCacheValue < 90 || dictionaryCacheValue < 90 ||
	    		inMemoryValue < 90){
			return true;
		}
	    
	    return false;
	}
	
	/**
	 * 
	* 1. 메소드명 : setTooltipTop3SQL
	* 2. 작성일 : 2015. 8. 25. 오후 10:18:42
	* 3. 작성자 : 길용현
	* 4. 설명 : bar chart 툴팁 설정
	* @param topSqlList
	 */
	private void setTooltipTop3SQL(XYChart.Series<String, Number> series, 
			ArrayList<TopSqlDto> topSqlList, int index){
		int cnt = 0;
		
	    for(final XYChart.Data<String,Number> data : series.getData()){
	    	Tooltip tooltip = new Tooltip();
	    	if(cnt==0){
	    		tooltip.setText("SQLID : " + topSqlList.get(index).getSqlId1());
	    	}else if(cnt==1){
	    		tooltip.setText("SQLID : " + topSqlList.get(index).getSqlId2());
	    	}else if(cnt==2){
	    		tooltip.setText("SQLID : " + topSqlList.get(index).getSqlId3());
	    	}
	        Tooltip.install(data.getNode(), tooltip);  
	        applyMouseEventsforTop3SQL(data);
	        cnt++;
	    }
	}
	
	/**
	 * 
	* 1. 메소드명 : applyMouseEventsforTop3SQL
	* 2. 작성일 : 2015. 8. 25. 오후 10:18:57
	* 3. 작성자 : 길용현
	* 4. 설명 : bar chart Node Mouse Event 설정
	* @param data
	 */
	private void applyMouseEventsforTop3SQL(final XYChart.Data data){
		final Node node = data.getNode();

		node.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				node.setEffect(new Glow());
			}
		});

		node.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				node.setEffect(null);
			}
		});

		node.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {

			}
		});
	}
	
	/**
	 * 
	* 1. 메소드명 : addLoadingData
	* 2. 작성일 : 2015. 8. 24. 오후 12:09:59
	* 3. 작성자 : 길용현
	* 4. 설명 : 초기 그래프 셋팅
	* @param cnt
	 */
	public void addLoadingData(int cnt) {
		if (cnt == 0) {
			increasefun = 0;
			decreasefun = 100;
		} else if (cnt % 5 == 0) {
			c++;
			increasefun = 33 * c;
			decreasefun = 100 - (33 * c);
		}
		
		// performence 항목 chart data 추가
		bufferCacheHitSeries.getData().add(new XYChart.Data<String, Number>(cnt + "",(Number) increasefun));
		libraryCacheHitSeries.getData().add(new XYChart.Data<String, Number>(cnt + "",(Number) decreasefun));
		dictionaryCacheHitSeries.getData().add(new XYChart.Data<String, Number>(cnt + "",(Number) increasefun));
		inMemoryHitSeries.getData().add(new XYChart.Data<String, Number>(cnt + "",(Number) decreasefun));

		// wait event 항목 chart data 추가
		bufferBusyWaitSeries.getData().add(new XYChart.Data<String, Number>(cnt + "",(Number) increasefun));
		logFileSyncSeries.getData().add(new XYChart.Data<String, Number>(cnt + "",(Number) decreasefun));
		dbFileSequentialSeries.getData().add(new XYChart.Data<String, Number>(cnt + "",(Number) increasefun));
		dbFileScatteredSeries.getData().add(new XYChart.Data<String, Number>(cnt + "",(Number) decreasefun));
		libraryCacheLockSeries.getData().add(new XYChart.Data<String, Number>(cnt + "",(Number) increasefun));
		logBufferSpaceSeries.getData().add(new XYChart.Data<String, Number>(cnt + "",(Number) decreasefun));
	}
	
	public void playSound(){
		mediaPlayer.play();
	}
	
	public void stopSound(){
		mediaPlayer.stop();
	}
}
