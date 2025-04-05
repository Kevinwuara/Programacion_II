package cl.kai.android.evaluacionunidad1

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cl.kai.restaurant.CuentaMesa
import cl.kai.restaurant.ItemMenu
import cl.kai.restaurant.ItemMesa
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var cuentaMesa: CuentaMesa
    private lateinit var pastelDeChoclo: ItemMenu
    private lateinit var cazuela: ItemMenu
    private lateinit var itemMesa1: ItemMesa
    private lateinit var itemMesa2: ItemMesa
    private lateinit var currencyFormat: NumberFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configuración de formato de moneda en pesos chilenos
        currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

        // Inicialización de objetos del menú
        pastelDeChoclo = ItemMenu("Pastel de Choclo", 12000)
        cazuela = ItemMenu("Cazuela", 10000)

        // Inicialización de ítems de la mesa y cuenta
        itemMesa1 = ItemMesa(pastelDeChoclo, 0)
        itemMesa2 = ItemMesa(cazuela, 0)
        cuentaMesa = CuentaMesa()

        // Referencias a elementos de la interfaz
        val etCantidadPlato1 = findViewById<EditText>(R.id.etCantidadPlato1)
        val etCantidadPlato2 = findViewById<EditText>(R.id.etCantidadPlato2)
        val etTotalPlato1 = findViewById<TextView>(R.id.etTotalPlato1)
        val etTotalPlato2 = findViewById<TextView>(R.id.etTotalPlato2)
        val etTotalComida = findViewById<TextView>(R.id.etTotalComida)
        val etPropina = findViewById<TextView>(R.id.etPropina)
        val etTOTAL = findViewById<TextView>(R.id.etTOTAL)
        val switchPropina = findViewById<Switch>(R.id.switchPropina)

        // Función para actualizar los totales en pantalla
        fun actualizarTotales() {
            // Validación: Solo usar cantidades válidas
            val cantidad1 = etCantidadPlato1.text.toString().toIntOrNull() ?: 0
            val cantidad2 = etCantidadPlato2.text.toString().toIntOrNull() ?: 0

            // Actualizar cantidades de los ítems
            itemMesa1.cantidad = cantidad1
            itemMesa2.cantidad = cantidad2

            // Recalcular subtotales
            val subtotalPlato1 = itemMesa1.calcularSubtotal()
            val subtotalPlato2 = itemMesa2.calcularSubtotal()

            // Mostrar los subtotales, y si las cantidades son cero, mostrar "$0"
            etTotalPlato1.text = currencyFormat.format(subtotalPlato1.toDouble())
            etTotalPlato2.text = currencyFormat.format(subtotalPlato2.toDouble())

            // Calcular el total sin propina sumando los subtotales
            val totalSinPropina = subtotalPlato1 + subtotalPlato2
            etTotalComida.text = currencyFormat.format(totalSinPropina.toDouble())

            // Calcular la propina (10%) si el Switch está activado
            val propina = if (switchPropina.isChecked && totalSinPropina > 0) (totalSinPropina * 0.1).toInt() else 0
            etPropina.text = currencyFormat.format(propina.toDouble())

            // Calcular el total final (incluyendo propina si aplica)
            val totalConPropina = totalSinPropina + propina
            etTOTAL.text = currencyFormat.format(totalConPropina.toDouble())
        }

        // Evento TextChanged para el campo de cantidad del plato 1
        etCantidadPlato1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                actualizarTotales()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Evento TextChanged para el campo de cantidad del plato 2
        etCantidadPlato2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                actualizarTotales()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Evento CheckedChange para el Switch de propina
        switchPropina.setOnCheckedChangeListener { _, _ ->
            actualizarTotales()
        }
    }
}