using System.Collections.Generic;
using WeatherApp.ViewModel;

namespace WeatherApp.Model
{
    public class City : DynamicViewModel
    {
        public string Name { get; set; }

        private string _Description;
        public string Description { get => _Description; set => SetProperty(ref _Description, value); }

        public string CountryCode { get; set; }

        public string CurrentTime { get; set; }

        // TEMPERATURES
        private string _CurrentTemp ;
        public string CurrentTemp { get => _CurrentTemp; set => SetProperty(ref _CurrentTemp, value); }

        private string _CurrentTempDiff;
        public string CurrentTempDiff { get => _CurrentTempDiff; set => SetProperty(ref _CurrentTempDiff, value); }

        // OTHER STATS
        private string _CurrentPressure;
        public string CurrentPressure { get => _CurrentPressure; set => SetProperty(ref _CurrentPressure, value); }

        private string _CurrentPreciptitation;
        public string CurrentPreciptitation { get => _CurrentPreciptitation; set => SetProperty(ref _CurrentPreciptitation, value); }

        private string _CurrentWind;
        public string CurrentWind { get => _CurrentWind; set => SetProperty(ref _CurrentWind, value); }

        private string _CurrentHumidity;
        public string CurrentHumidity { get => _CurrentHumidity; set => SetProperty(ref _CurrentHumidity, value); }

        public List<City> GetAllCities()
        {
            List<City> cities = new List<City>()
            {
                new City()
                {
                    Name="Aveiro",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Beja",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Braga",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Bragança",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Castelo Branco",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Coimbra",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Évora",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Faro",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Guarda",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Leiria",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Lisboa",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Portalegre",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Porto",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Santarém",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Setúbal",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Viana do Castelo",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Vila Real",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Viseu",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                }
            };
            return cities;
        }
    }
}
