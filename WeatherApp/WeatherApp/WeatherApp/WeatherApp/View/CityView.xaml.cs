using SkiaSharp;
using SkiaSharp.Views.Forms;
using System;
using System.Linq;
using WeatherApp.ViewModel;
using WeatherApp.Model;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp.View
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class CityView : ContentPage
    {
        DetailedCityViewModel vm;

        public CityView(CityViewModel city)
        {
            vm = new DetailedCityViewModel(city);
            vm.view = this;
            vm.getCityDetails();

            InitializeComponent();

            BindingContext = vm.cityVM;
            canvas.PaintSurface += OnPaint;
        }

        public void UpdateChart()
        {
            // To force redraw of canvas
            canvas.InvalidateSurface();

            for (int i = 0; i < vm.cityVM.Icons.Count(); i++)
            {
                Image img = new Image
                {
                    Source = vm.cityVM.Icons[i],
                    Aspect = Aspect.AspectFit
                };

                Grid.SetColumn(img, i + 1);
                //Grid.SetRow(img, 0);
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

            DrawGraph(
                canvas, new SKPoint(topLeftX, topLeftY), new SKPoint(bottomRightX, bottomRightY),
                vm.cityVM.Hours, vm.cityVM.Temps, vm.cityVM.Precipitations
            );
        }

        void DrawGraph(SKCanvas canvas, SKPoint topLeft, SKPoint bottomRight, string[] hours, float[] temps, float[] precipitation)
        {
            SKPaint coorPaint = new SKPaint
            {      // paint for the axis and text
                Style = SKPaintStyle.StrokeAndFill,
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
                Color = SKColors.OrangeRed,
                StrokeWidth = 5,
                StrokeCap = SKStrokeCap.Round,
                TextSize = 30
            };

            SKPaint graphPaint2 = new SKPaint
            {      // paint for the axis and text
                Style = SKPaintStyle.StrokeAndFill,
                Color = SKColors.OrangeRed,
                StrokeWidth = 5,
                StrokeCap = SKStrokeCap.Round,
                TextSize = 30
            };

            SKPaint graphPaint3 = new SKPaint
            {      // paint for the axis and text
                Style = SKPaintStyle.Stroke,
                Color = SKColors.LightSteelBlue,
                StrokeWidth = 5,
                StrokeCap = SKStrokeCap.Round,
                TextSize = 30
            };

            SKPaint graphPaint4 = new SKPaint
            {      // paint for the axis and text
                Style = SKPaintStyle.StrokeAndFill,
                Color = SKColors.LightSteelBlue,
                StrokeWidth = 5,
                StrokeCap = SKStrokeCap.Round,
                TextSize = 30
            };

            SKPoint origin = new SKPoint(topLeft.X, bottomRight.Y);

            //draw X, Y and 2nd Y axis;
            canvas.DrawLine(origin, bottomRight, coorPaint);
            canvas.DrawLine(origin, new SKPoint(topLeft.X, topLeft.Y - 30), coorPaint);
            canvas.DrawLine(new SKPoint(bottomRight.X, origin.Y), new SKPoint(bottomRight.X, topLeft.Y - 30), coorPaint);

            //draw hour text
            int steps = hours.Count();
            float stepX = (bottomRight.X - origin.X) / steps;
            for(int i = 0; i < steps; i++)
            {
                canvas.DrawLine(new SKPoint(origin.X + i * stepX, origin.Y), new SKPoint(origin.X + i * stepX, origin.Y + 15), coorPaint); //draw guide lines on X axis
                canvas.DrawText(hours[i], origin.X + i * stepX + stepX / 5, origin.Y + 40, coorPaint); //draw '00h' text
            }

            //calculate temperature value per pixel in Y axis
            float maxTemp = temps.Max();
            float minTemp = temps.Min();
            float pixelPerDegree = Math.Abs((topLeft.Y - origin.Y) / (maxTemp - minTemp));

            //calculate temperature value per pixel in Y2 axis
            float maxPrecipitation = precipitation.Max();
            float minPrecipitation = precipitation.Min();
            float pixelPerPrecipitationMM = Math.Abs((topLeft.Y - origin.Y) / (maxPrecipitation - minPrecipitation));

            float stepY = (origin.Y - topLeft.Y) / 4;

            //draw guide lines on Y axis
            for(int i = 0; i < 4; i++)
            {
                float guidelineTemp = maxTemp - i * ((maxTemp - minTemp) / 4);
                canvas.DrawLine(new SKPoint(origin.X, topLeft.Y + i * stepY), new SKPoint(bottomRight.X, topLeft.Y + i * stepY), coorPaint2);
                if(guidelineTemp != maxTemp && guidelineTemp != minTemp)
                {
                    canvas.DrawText(guidelineTemp.ToString("0.0"), origin.X - 80, topLeft.Y + 30 + i * stepY, coorPaint);
                }
            }

            canvas.DrawLine(origin, new SKPoint(origin.X - 15, origin.Y), coorPaint);
            canvas.DrawLine(new SKPoint(topLeft.X, topLeft.Y), new SKPoint(topLeft.X - 15, topLeft.Y), coorPaint);
            canvas.DrawText(minTemp.ToString("0.0"), origin.X - 80, origin.Y - 15, coorPaint); //estes + e - 15 são ajustes para tornar o número mais legível
            canvas.DrawText(maxTemp.ToString("0.0"), topLeft.X - 80, topLeft.Y + 15, coorPaint);

            //draw guide text on Y2 axis
            for (int i = 0; i < 4; i++)
            {
                float guidelinePrecipitation = maxPrecipitation - i * ((maxPrecipitation - minPrecipitation) / 4);
                if (guidelinePrecipitation != maxPrecipitation && guidelinePrecipitation != maxPrecipitation)
                {
                    canvas.DrawText(guidelinePrecipitation.ToString("0.00"), bottomRight.X + 10, topLeft.Y + 30 + i * stepY, coorPaint);
                }
            }

            canvas.DrawLine(bottomRight, new SKPoint(bottomRight.X + 15, bottomRight.Y), coorPaint);
            canvas.DrawLine(new SKPoint(bottomRight.X, topLeft.Y), new SKPoint(bottomRight.X + 15, topLeft.Y), coorPaint);
            canvas.DrawText(minPrecipitation.ToString("0.00"), bottomRight.X + 10, origin.Y - 15, coorPaint); //estes + e - 15 são ajustes para tornar o número mais legível
            canvas.DrawText(maxPrecipitation.ToString("0.00"), bottomRight.X + 10, topLeft.Y + 15, coorPaint);


            //draw temperature path
            SKPath path = new SKPath();

            path.MoveTo(origin.X + stepX / 2, origin.Y - (temps[0] - minTemp) * pixelPerDegree);
            canvas.DrawCircle(new SKPoint(origin.X + stepX / 2, origin.Y - (temps[0] - minTemp) * pixelPerDegree), 10, graphPaint2);

            for(int i = 1; i < temps.Count(); i++)
            {
                canvas.DrawCircle(new SKPoint(origin.X + i * stepX + stepX / 2, origin.Y - (temps[i] - minTemp) * pixelPerDegree), 10, graphPaint2);
                path.LineTo(origin.X + i * stepX + stepX / 2, origin.Y - (temps[i] - minTemp) * pixelPerDegree);
            }
            canvas.DrawPath(path, graphPaint);

            //draw precipitation path
            SKPath path2 = new SKPath();

            path2.MoveTo(origin.X + stepX / 2, origin.Y - (precipitation[0] - minPrecipitation) * pixelPerPrecipitationMM);
            canvas.DrawCircle(new SKPoint(origin.X + stepX / 2, origin.Y - (precipitation[0] - minPrecipitation) * pixelPerPrecipitationMM), 10, graphPaint4);

            for (int i = 1; i < temps.Count(); i++)
            {
                canvas.DrawCircle(new SKPoint(origin.X + i * stepX + stepX / 2, origin.Y - (precipitation[i] - minPrecipitation) * pixelPerPrecipitationMM), 10, graphPaint4);
                path2.LineTo(origin.X + i * stepX + stepX / 2, origin.Y - (precipitation[i] - minPrecipitation) * pixelPerPrecipitationMM);
            }
            canvas.DrawPath(path2, graphPaint3);

            //draw graph info
            SKPath path3 = new SKPath();
            path3.MoveTo(origin.X + 100, topLeft.Y - 100);
            path3.LineTo(origin.X + 115, topLeft.Y - 100);
            canvas.DrawCircle(new SKPoint(origin.X + 115, topLeft.Y - 100), 7, graphPaint2);
            path3.LineTo(origin.X + 130, topLeft.Y - 100);
            canvas.DrawPath(path3, graphPaint);
            canvas.DrawText("Temperature - ºC (Left Axis)", new SKPoint(origin.X + 140, topLeft.Y - 95), coorPaint);

            SKPath path4 = new SKPath();
            path4.MoveTo(origin.X + 100, topLeft.Y - 50);
            path4.LineTo(origin.X + 115, topLeft.Y - 50);
            canvas.DrawCircle(new SKPoint(origin.X + 115, topLeft.Y - 50), 7, graphPaint4);
            path4.LineTo(origin.X + 130, topLeft.Y - 50);
            canvas.DrawPath(path4, graphPaint3);
            canvas.DrawText("Precipitation - mm (Right Axis)", new SKPoint(origin.X + 140, topLeft.Y - 45), coorPaint);
        }

        private void ImageButton_Clicked(object sender, EventArgs e)
        {
            Navigation.PopModalAsync();
        }
    }
}