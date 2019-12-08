using SkiaSharp;
using SkiaSharp.Views.Forms;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp.View
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class CityView : ContentPage
    {
        readonly int ncycles = 8;
        readonly int graphH = 500;
        readonly int margin = 10;
        public CityView()
        {
            InitializeComponent();
            canvas.PaintSurface += OnPaint;
        }

        public void OnPaint(object sender, SKPaintSurfaceEventArgs args)
        {
            int wd = args.Info.Width;
            int hg = args.Info.Height;
            SKCanvas cnv = args.Surface.Canvas;

            cnv.Clear();

            float pxP = (float)(wd - 4 * margin) / ncycles;    // pixel size for 1 period on the Y axis
            float one, xhg;

            DrawAxis(cnv, wd, hg, pxP, out one, out xhg);
            DrawGraph(cnv, wd, hg, pxP, one, xhg);
        }

        void DrawAxis(SKCanvas cnv, int wd, int hg, float pxP, out float one, out float xhg)
        {
            int ylen;    // total size of the Y axis (in pixels) with a maximum of 500px for 1 and another 500px for -1 + 20px after 1 and -1

            SKPaint coorPaint = new SKPaint
            {      // paint for the axis and text
                Style = SKPaintStyle.Stroke,
                Color = SKColors.Blue,
                StrokeWidth = 2,
                StrokeCap = SKStrokeCap.Square,
                TextSize = 2 * margin
            };

            ylen = Math.Min(2 * graphH + 4 * margin, hg - 2 * margin);
            one = (float)ylen - 4 * margin;
            xhg = (float)hg - (hg - ylen) / 2 - 2 * margin;

            cnv.DrawLine(margin, xhg - one, 3 * margin, xhg - one, coorPaint);  // 1 mark on the Y axis
            cnv.DrawText("1", 3.5f * margin, xhg - one, coorPaint);             // 1 label on the Y axis
            for (int k = 1; k <= ncycles; k++)
            {                                // marks and labels on the X axis
                cnv.DrawLine(2 * margin + k * pxP, xhg - margin / 2, 2 * margin + k * pxP, xhg + margin / 2, coorPaint);
                cnv.DrawText(k.ToString(), 1.5f * margin + k * pxP, xhg + 3 * margin, coorPaint);
            }
            cnv.DrawText("periods", 3 * margin, xhg + 2.5f * margin, coorPaint);     // 'periods' indication on the X axis
        }

        void DrawGraph(SKCanvas cnv, int wd, int hg, float pxP, float one, float yhg)
        {
            SKPaint gPaint = new SKPaint
            {        // paint for the graphic
                Style = SKPaintStyle.Stroke,
                Color = SKColors.Red,
                StrokeWidth = 1,
                StrokeCap = SKStrokeCap.Butt
            };

            float period = (float)(2 * Math.PI);     // period (math in radians) for the Sinus
            SKPath path = new SKPath();
            path.MoveTo(2 * margin, yhg);            // point (0, 0) on the graphic

            cnv.DrawPath(path, gPaint);
        }

        /*void OnCanvasViewPaintSurface(object sender, SKPaintSurfaceEventArgs args)
        {
            SKImageInfo info = args.Info;
            SKSurface surface = args.Surface;
            SKCanvas canvas = surface.Canvas;

            canvas.Clear();

            // Create the path
            SKPath path = new SKPath();

            // Define the first contour
            path.MoveTo(0.5f * info.Width, 0.1f * info.Height);
            path.LineTo(0.2f * info.Width, 0.4f * info.Height);
            path.LineTo(0.8f * info.Width, 0.4f * info.Height);
            path.LineTo(0.5f * info.Width, 0.1f * info.Height);

            // Define the second contour
            path.MoveTo(0.5f * info.Width, 0.6f * info.Height);
            path.LineTo(0.2f * info.Width, 0.9f * info.Height);
            path.LineTo(0.8f * info.Width, 0.9f * info.Height);
            path.Close();

            // Create two SKPaint objects
            SKPaint strokePaint = new SKPaint
            {
                Style = SKPaintStyle.Stroke,
                Color = SKColors.Magenta,
                StrokeWidth = 50
            };

            SKPaint fillPaint = new SKPaint
            {
                Style = SKPaintStyle.Fill,
                Color = SKColors.Cyan
            };

            // Fill and stroke the path
            canvas.DrawPath(path, fillPaint);
            canvas.DrawPath(path, strokePaint);
        }*/
    }
}