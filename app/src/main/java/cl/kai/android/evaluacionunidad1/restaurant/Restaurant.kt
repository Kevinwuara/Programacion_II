package cl.kai.restaurant

// Clase ItemMenu
class ItemMenu(val nombre: String, val precio: Int) {
    override fun toString(): String {
        return "$nombre - $$precio"
    }
}

// Clase ItemMesa
class ItemMesa(val itemMenu: ItemMenu, var cantidad: Int) {
    fun calcularSubtotal(): Int {
        return cantidad * itemMenu.precio
    }
}

// Clase CuentaMesa
class CuentaMesa(val aceptaPropina: Boolean = true) {
    private val items = mutableListOf<ItemMesa>()

    fun agregarItem(itemMesa: ItemMesa) {
        items.add(itemMesa)
    }

    fun calcularTotalSinPropina(): Int {
        return items.sumOf { it.calcularSubtotal() }
    }

    fun calcularPropina(): Int {
        return if (aceptaPropina) (calcularTotalSinPropina() * 0.1).toInt() else 0
    }

    fun calcularTotalConPropina(): Int {
        return calcularTotalSinPropina() + calcularPropina()
    }

    override fun toString(): String {
        val detallesItems = items.joinToString("\n") {
            "${it.itemMenu.nombre} x ${it.cantidad} - Subtotal: ${it.calcularSubtotal()}"
        }
        val propina = calcularPropina()
        val totalConPropina = calcularTotalConPropina()
        return "Pedido:\n$detallesItems\n" +
                "Total sin propina: ${calcularTotalSinPropina()}\n" +
                "Propina (10%): $propina\n" +
                "Total con propina: $totalConPropina"
    }
}