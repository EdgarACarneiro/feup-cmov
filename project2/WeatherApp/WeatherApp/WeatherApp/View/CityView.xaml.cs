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
            string[] hours = { "00h", "03h", "06h", "09h", "12h", "15h", "18h", "21h" };
            float[] temps = { 9, 12.4f, 11.3f, 11, 11.1f, 10.7f, 10.1f, 9.4f };

            int topLeftX = (int)Math.Round(args.Info.Width * 0.08);
            int topLeftY = (int)Math.Round(args.Info.Height * 0.08);
            int bottomRightX = (int)Math.Round(args.Info.Width * 0.95);
            int bottomRightY = (int)Math.Round(args.Info.Height * 0.90);

            SKCanvas canvas = args.Surface.Canvas;

            canvas.Clear();

            //canvas.DrawLine(new SKPoint(originX, originY), new SKPoint(maxX, maxY), coorPaint);
            DrawAxis(canvas, new SKPoint(topLeftX, topLeftY), new SKPoint(bottomRightX, bottomRightY), hours, temps);
            //DrawAxis(args.Surface.Canvas, hours, temps, )
        }

        void DrawAxis(SKCanvas canvas, SKPoint topLeft, SKPoint bottomRight, string[] hours, float[] temps)
        {
            SKPaint coorPaint = new SKPaint
            {      // paint for the axis and text
                Style = SKPaintStyle.Stroke,
                Color = SKColors.Black,
                StrokeWidth = 2,
                StrokeCap = SKStrokeCap.Round,
                TextSize = 30
            };

            SKPoint origin = new SKPoint(topLeft.X, bottomRight.Y);

            //draw X and Y axis;
            canvas.DrawLine(origin, bottomRight, coorPaint);
            canvas.DrawLine(origin, topLeft, coorPaint);

            //draw hour text
            int steps = hours.Count();
            float stepX = (bottomRight.X - origin.X) / steps;
            for(int i = 0; i < steps; i++)
            {
                canvas.DrawLine(new SKPoint(origin.X + i * stepX, origin.Y), new SKPoint(origin.X + i * stepX, origin.Y + 15), coorPaint); //draw guide lines on X axis
                canvas.DrawText(hours[i], origin.X + i * stepX + 5, origin.Y + 35, coorPaint); //draw '00h' text
            }

            //draw guide lines on Y axis
            float maxTemp = temps.Max();
            float minTemp = temps.Min();

            canvas.DrawLine(origin, new SKPoint(origin.X - 15, origin.Y), coorPaint);
            canvas.DrawLine(new SKPoint(topLeft.X, topLeft.Y + 30), new SKPoint(topLeft.X - 15, topLeft.Y + 30), coorPaint);
            canvas.DrawText(minTemp.ToString(), origin.X - 60, origin.Y - 15, coorPaint); //estes + e - 15 são ajustes para tornar o número mais legível
            canvas.DrawText(maxTemp.ToString(), topLeft.X - 60, topLeft.Y + 15, coorPaint);

            //calculate temperature value per pixel in Y axis
            float pixelPerDegree = (topLeft.Y - origin.Y) / (maxTemp - minTemp);

            for(int i = 0; i < temps.Count(); i++)
            {
                if(temps[i] != minTemp && temps[i] != maxTemp)
                {
                    canvas.DrawText(temps[i].ToString(), origin.X - 60, origin.Y + (temps[i] - minTemp) * pixelPerDegree - 15, coorPaint);
                }
            }
        }

        /*void DrawAxis(SKCanvas cnv, int wd, int hg, float pxP, out float one, out float xhg)
        {
            
            int ylen;    // total size of the Y axis (in pixels) with a maximum of 500px for 1 and another 500px for -1 + 20px after 1 and -1

            SKPaint coorPaint = new SKPaint
            {      // paint for the axis and text
                Style = SKPaintStyle.Stroke,
                Color = SKColors.Black,
                StrokeWidth = 3,
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

        void OnCanvasViewPaintSurface(object sender, SKPaintSurfaceEventArgs args)
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