using System;
using System.Collections.Generic;
using System.Text;
using WeatherApp.Model;

namespace WeatherApp.ViewModel
{
    public class CityViewModel
    {
        public List<City> Cities { get; set; }
        public CityViewModel()
        {
            Cities = new City().GetCities();
        }
    }
}
