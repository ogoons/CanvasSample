package com.ogoons.canvassample

import android.content.Context
import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Paint.Align
import android.view.View


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(SampleView(this))
    }


    private class SampleView/*
         * 생성자에서 초기화 처리를 하고있습니다.
         * 그리기 도구인 Paint 객체를 설정하고 있습니다.
         * 선의 모서리를 부드럽게 하고, 선두께, 글자크기,
         * 글자정렬 등을 설정하고, 선 객체를 생성하고 있습니다.
         */
        (context: Context) : View(context) {
        private val mPaint: Paint
        private val mPath: Path

        init {
            setFocusable(true)

            mPaint = Paint()
            mPaint.setAntiAlias(true) //모서리 부드럽게
            mPaint.setStrokeWidth(6F) //선두께
            mPaint.setTextSize(16F)  //글자크기
            mPaint.setTextAlign(Paint.Align.RIGHT) //글자정렬

            // 선객체
            mPath = Path()
        }

        /*
         * 임의로 설정한 canvas 객체를 받아서,
         * 캔버스에 사각형과 원, 텍스트를 출력합니다.
         * 여기서는 그냥 전체 영역을 출력합니다.
         * 클립영역 설정을 하는 부분은, 이 메소드를
         * 호출하기 전에, canvas 객체에 설정을 하고
         * 이 메소드로 넘기고 있습니다.
         */
        private fun drawScene(canvas: Canvas) {
            canvas.clipRect(0, 0, 100, 100)

            canvas.drawColor(Color.WHITE)

            mPaint.setColor(Color.RED)
            canvas.drawLine(0F, 0F, 100F, 100F, mPaint)

            mPaint.setColor(Color.GREEN)
            canvas.drawCircle(30F, 70F, 30F, mPaint)

            mPaint.setColor(Color.BLUE)
            canvas.drawText("Clipping", 100F, 30F, mPaint)
        }

        /*
         * (non-Javadoc)
         * @see android.view.View#onDraw(android.graphics.Canvas)
         * 시스템이 액티비티 화면에 그리기를 시도할때 호출하는 메소드 입니다.
         */
        override fun onDraw(canvas: Canvas) {
            canvas.drawColor(Color.GRAY)
            /*
             * canvas.save() 메소드는 현재 기본 설정을 저장했다가,
             * 임의로 설정을 하여 그리기를 한 후에, 다시 restore 메소드로 복구합니다.
             * 캔버스 객체는 로컬좌표를 사용합니다. 이때 translate 메소드로 좌표를
             * 설쟁해주면, 그 좌표가 0,0 이 되어, 이 설정후에 그리기를 시도하면,
             * 모두 10,10 을 원점(0,0)으로 하여 그리기를 수행하게 됩니다.
             */
            canvas.save()
            canvas.translate(10F, 10F)
            drawScene(canvas)
            canvas.restore()

            /*
             * clipRect 메소드에서 설정한 사각영역만이 전체영역에서 출력이 됩니다.
             * 다른부분은 그냥 배경화면이 출력됩니다.
             * 두 사각형 클립 영역중에서 Region.Op.DIFFERENCE 옵션이 아닌 영역에서
             * Region.Op.DIFFERENCE 영역을 뺀 영역이 바로 클립영역이 되어, 그 부분만
             * 출력이 됩니다.
             */
            canvas.save()
            canvas.translate(160F, 10F)
            canvas.clipRect(10, 10, 90, 90)
            canvas.clipRect(30F, 30F, 70F, 70F, Region.Op.DIFFERENCE)
            drawScene(canvas)
            canvas.restore()

            canvas.save()
            // 여기서부터 캔버스의 원점좌표(0,0)는 현 액티비티 화면의 10,160 좌표지점이 된다.
            // 캔버스는 상대좌표임.. 디폴트로는 액티비티 화면의 원점좌표와 동일
            canvas.translate(10F, 160F)
            mPath.reset()
            canvas.clipPath(mPath) // makes the clip empty
            // 클립영역으로 원이 출력이 됩니다.
            // 원 부분만 출력이 되고 나머지는 배경이 나오게 됩니다.
            mPath.addCircle(50F, 50F, 50F, Path.Direction.CCW)
            canvas.clipPath(mPath, Region.Op.REPLACE)
            drawScene(canvas)
            canvas.restore()

            canvas.save()
            canvas.translate(160F, 160F)
            canvas.clipRect(0, 0, 60, 60)
            // 두 클립 영역의 합집합 입니다. 두영역 모두 클리핑 영역입니다.
            canvas.clipRect(40F, 40F, 100F, 100F, Region.Op.UNION)
            drawScene(canvas)
            canvas.restore()

            canvas.save()
            canvas.translate(10F, 310F)
            // 두 영역중에 겹치는 영역을 제외한 나머지 영역이 모두 클리핑 영엽입니다.
            canvas.clipRect(0, 0, 60, 60)
            canvas.clipRect(40F, 40F, 100F, 100F, Region.Op.XOR)
            drawScene(canvas)
            canvas.restore()

            canvas.save()
            canvas.translate(160F, 310F)
            // 두 영역중 Region.Op.REVERSE_DIFFERENCE 옵션인 영역에서
            // 그 옵션이 아닌 영역을 제외한 영역이 클리핑 영역입니다.
            canvas.clipRect(0, 0, 60, 60)
            canvas.clipRect(40F, 40F, 100F, 100F, Region.Op.REVERSE_DIFFERENCE)
            drawScene(canvas)
            canvas.restore()
        }
    }
}
