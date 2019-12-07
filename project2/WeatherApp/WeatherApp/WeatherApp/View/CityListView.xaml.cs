using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Threading.Tasks;
using WeatherApp.View;
using WeatherApp.ViewModel;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class CityListView : ContentPage
    {
        public ObservableCollection<string> Items { get; set; }
        CityViewModel vm;

        public CityListView()
        {
            InitializeComponent();

            vm = new CityViewModel();
            cityList.ItemsSource = vm.Cities;
            cityPicker.ItemsSource = vm.AllCities;
        }

        async void Handle_ItemTapped(object sender, ItemTappedEventArgs e)
        {
            if (e.Item == null)
                return;

            //await DisplayAlert("Item Tapped", "An item was tapped.", "OK");
            await Navigation.PushModalAsync(new NavigationPage(new CityView()));

            //Deselect Item
            ((ListView)sender).SelectedItem = null;
        }

        private void Button_Clicked(object sender, EventArgs e)
        {

        }
    }
}
