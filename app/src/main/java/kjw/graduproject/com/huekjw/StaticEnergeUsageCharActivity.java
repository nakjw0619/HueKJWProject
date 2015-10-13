package kjw.graduproject.com.huekjw;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.philips.lighting.hue.sdk.utilities.impl.Color;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

// 그래프 그리는 부분
public class StaticEnergeUsageCharActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_energe_usage_char);

        // 표시할 수치값
        List<double[]> values = new ArrayList<double[]>();
        values.add(new double[] { 10, 20, 30, 30, 20, 10,
                20, 40, 10, 20, 30, 30});

        /** 그래프 출력을 위한 그래픽 속성 지정객체 */
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        // 상단 표시 제목과 글자 크기
        renderer.setChartTitle("Hue Enery Usage");
        renderer.setChartTitleTextSize(40);

        // 분류에 대한 이름
        String[] titles = new String[] { "Monthly Usage" };

        // 항목을 표시하는데 사용될 색상값
        int[] colors = new int[] { Color.YELLOW };

        // 분류명 글자 크기 및 각 색상 지정
        renderer.setLegendTextSize(30);
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
        }

        // X,Y축 항목이름과 글자 크기
        renderer.setXTitle("Months");
        renderer.setYTitle("Times");
        renderer.setAxisTitleTextSize(24);

        // 수치값 글자 크기 / X축 최소,최대값 / Y축 최소,최대값
        renderer.setLabelsTextSize(10);
        renderer.setXAxisMin(0.5);
        renderer.setXAxisMax(12.5);
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(100);

        // X,Y축 라인 색상
        renderer.setAxesColor(Color.WHITE);
        // 상단제목, X,Y축 제목, 수치값의 글자 색상
        renderer.setLabelsColor(Color.CYAN);

        // X축의 표시 간격
        renderer.setXLabels(12);
        // Y축의 표시 간격
        renderer.setYLabels(5);

        // X,Y축 정렬방향
        renderer.setXLabelsAlign(Paint.Align.LEFT);
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        // X,Y축 스크롤 여부 ON/OFF
        renderer.setPanEnabled(false, false);
        // ZOOM기능 ON/OFF
        renderer.setZoomEnabled(false, false);
        // ZOOM 비율
        renderer.setZoomRate(1.0f);
        // 막대간 간격
        renderer.setBarSpacing(0.5f);

        // 설정 정보 설정
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        for (int i = 0; i < titles.length; i++) {
            CategorySeries series = new CategorySeries(titles[i]);
            double[] v = values.get(i);
            int seriesLength = v.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(v[k]);
            }
            dataset.addSeries(series.toXYSeries());
        }

        // 그래프 객체 생성
        GraphicalView gv = ChartFactory.getBarChartView(this, dataset,
                renderer, BarChart.Type.STACKED);

        // 그래프를 LinearLayout에 추가
        LinearLayout llBody = (LinearLayout) findViewById(R.id.llBody);
        llBody.addView(gv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_static_energe_usage_char, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
