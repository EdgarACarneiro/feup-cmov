using SkiaSharp;
using SkiaSharp.Views.Forms;
using System;
using System.Linq;
using WeatherApp.ViewModel;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp.View
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class CityView : ContentPage
    {
        DetailedCityViewModel vm;

        public CityView(DetailedCityViewModel vm)
        {
            vm.view = this;
            vm.getCityDetails();
            this.vm = vm;
            BindingContext = vm.city;

            InitializeComponent();
            canvas.PaintSurface += OnPaint;
        }

        public void SetGraphIcons()
        {
            for(int i = 0; i < vm.city.Icons.Count(); i++)
            {
                Image img = new Image
                {
                    Source = vm.city.Icons[i],
                    Aspect = Aspect.AspectFill
                };

                Grid.SetColumn(img, i + 1);
                graph_images.Children.Add(img);
            }
        }

        public void Handle_ItemTapped(object sender, ItemTappedEventArgs e)
        {
            if (e.Item == null)
                return;

            //Deselect Item
            ((ListView)sender).SelectedItem = null;
        }

        public void OnPaint(object sender, SKPaintSurfaceEventArgs args)
        {
            int topLeftX = (int)Math.Round(args.Info.Width * 0.08);
            int topLeftY = (int)Math.Round(args.Info.Height * 0.08);
            int bottomRightX = (int)Math.Round(args.Info.Width * 0.95);
            int bottomRightY = (int)Math.Round(args.Info.Height * 0.93);

            SKCanvas canvas = args.Surface.Canvas;

            canvas.Clear();

            //canvas.DrawLine(new SKPoint(originX, originY), new SKPoint(maxX, maxY), coorPaint);
            DrawGraph(canvas, new SKPoint(topLeftX, topLeftY), new SKPoint(bottomRightX, bottomRightY), vm.city.Hours, vm.city.Temps);
            //DrawAxis(args.Surface.Canvas, hours, temps, )
        }

        void DrawGraph(SKCanvas canvas, SKPoint topLeft, SKPoint bottomRight, string[] hours, float[] temps)
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
                Color = SKColors.LightGray,
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
            canvas.DrawLine(origin, new SKPoint(topLeft.X, topLeft.Y - 30), coorPaint);

            //draw hour text
            int steps = hours.Count();
            float stepX = (bottomRight.X - origin.X) / steps;
            for(int i = 0; i < steps; i++)
            {
                canvas.DrawLine(new SKPoint(origin.X + i * stepX, origin.Y), new SKPoint(origin.X + i * stepX, origin.Y + 15), coorPaint); //draw guide lines on X axis
                canvas.DrawText(hours[i], origin.X + i * stepX + stepX / 5, origin.Y + 40, coorPaint); //draw '00h' text
            }

            float maxTemp = temps.Max();
            float minTemp = temps.Min();
            //calculate temperature value per pixel in Y axis
            float pixelPerDegree = Math.Abs((topLeft.Y - origin.Y) / (maxTemp - minTemp));
            float stepY = (origin.Y - topLeft.Y) / 4;

            //draw guide lines on Y axis
            for(int i = 0; i < 4; i++)
            {
                float guidelineTemp = maxTemp - i * ((maxTemp - minTemp) / 4);
                canvas.DrawLine(new SKPoint(origin.X, topLeft.Y + i * stepY), new SKPoint(bottomRight.X, topLeft.Y + i * stepY), coorPaint2);
                if(guidelineTemp != maxTemp && guidelineTemp != minTemp)
                {
                    canvas.DrawText(guidelineTemp.ToString(), origin.X - 80, topLeft.Y + 30 + i * stepY, coorPaint);
                }
            }

            canvas.DrawLine(origin, new SKPoint(origin.X - 15, origin.Y), coorPaint);
            canvas.DrawLine(new SKPoint(topLeft.X, topLeft.Y), new SKPoint(topLeft.X - 15, topLeft.Y), coorPaint);
            canvas.DrawText(minTemp.ToString(), origin.X - 80, origin.Y - 15, coorPaint); //estes + e - 15 são ajustes para tornar o número mais legível
            canvas.DrawText(maxTemp.ToString(), topLeft.X - 80, topLeft.Y + 15, coorPaint);

            SKPath path = new SKPath();

            path.MoveTo(origin.X + stepX / 2, origin.Y - (temps[0] - minTemp) * pixelPerDegree);
            canvas.DrawCircle(new SKPoint(origin.X + stepX / 2, origin.Y - (temps[0] - minTemp) * pixelPerDegree), 10, graphPaint2);

            for(int i = 1; i < temps.Count(); i++)
            {
                canvas.DrawCircle(new SKPoint(origin.X + i * stepX + stepX / 2, origin.Y - (temps[i] - minTemp) * pixelPerDegree), 10, graphPaint2);
                path.LineTo(origin.X + i * stepX + stepX / 2, origin.Y - (temps[i] - minTemp) * pixelPerDegree);
            }
            //path.Close();
            canvas.DrawPath(path, graphPaint);
        }
    }
}