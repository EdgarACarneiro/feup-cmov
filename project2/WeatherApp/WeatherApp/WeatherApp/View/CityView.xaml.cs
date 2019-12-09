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

            SKPaint coorPaint2 = new SKPaint
            {      // paint for the axis and text
                Style = SKPaintStyle.Stroke,
                Color = SKColors.Gray,
                StrokeWidth = 2,
                StrokeCap = SKStrokeCap.Round,
                TextSize = 30
            };

            SKPaint graphPaint = new SKPaint
            {      // paint for the axis and text
                Style = SKPaintStyle.Stroke,
                Color = SKColors.Blue,
                StrokeWidth = 5,
                StrokeCap = SKStrokeCap.Round,
                TextSize = 30
            };

            SKPaint graphPaint2 = new SKPaint
            {      // paint for the axis and text
                Style = SKPaintStyle.StrokeAndFill,
                Color = SKColors.Blue,
                StrokeWidth = 5,
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
                canvas.DrawText(hours[i], origin.X + i * stepX + stepX / 3, origin.Y + 40, coorPaint); //draw '00h' text
            }

            float maxTemp = temps.Max();
            float minTemp = temps.Min();
            //calculate temperature value per pixel in Y axis
            float pixelPerDegree = (topLeft.Y - origin.Y) / (maxTemp - minTemp);
            float stepY = (origin.Y - topLeft.Y) / 4;

            //draw guide lines on Y axis
            for(int i = 0; i < 4; i++)
            {
                float guidelineTemp = maxTemp - i * ((maxTemp - minTemp) / 4);
                canvas.DrawLine(new SKPoint(origin.X, topLeft.Y + 30 + i * stepY), new SKPoint(bottomRight.X, topLeft.Y + 30 + i * stepY), coorPaint2);
                if(guidelineTemp != maxTemp && guidelineTemp != minTemp)
                {
                    canvas.DrawText(guidelineTemp.ToString(), origin.X - 80, topLeft.Y + 30 + i * stepY, coorPaint);
                }
            }

            canvas.DrawLine(origin, new SKPoint(origin.X - 15, origin.Y), coorPaint);
            canvas.DrawLine(new SKPoint(topLeft.X, topLeft.Y + 30), new SKPoint(topLeft.X - 15, topLeft.Y + 30), coorPaint);
            canvas.DrawText(minTemp.ToString(), origin.X - 80, origin.Y - 15, coorPaint); //estes + e - 15 são ajustes para tornar o número mais legível
            canvas.DrawText(maxTemp.ToString(), topLeft.X - 80, topLeft.Y + 15, coorPaint);

            SKPath path = new SKPath();

            path.MoveTo(origin.X + stepX / 2, origin.Y + (temps[0] - minTemp) * pixelPerDegree);
            canvas.DrawCircle(new SKPoint(origin.X + stepX / 2, origin.Y + (temps[0] - minTemp) * pixelPerDegree), 10, graphPaint2);

            for(int i = 1; i < temps.Count(); i++)
            {
                canvas.DrawCircle(new SKPoint(origin.X + i * stepX + stepX / 2, origin.Y + (temps[i] - minTemp) * pixelPerDegree + 30), 10, graphPaint2);
                path.LineTo(origin.X + i * stepX + stepX / 2, origin.Y + (temps[i] - minTemp) * pixelPerDegree + 30);
            }
            //path.Close();
            canvas.DrawPath(path, graphPaint);
        }
    }
}