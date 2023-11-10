package com.conteabe.conteabe

import com.conteabe.conteabe.dao.EmployeDAO
import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ServiceBD
import javafx.beans.binding.Bindings
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.scene.control.DatePicker
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.control.cell.CheckBoxListCell
import java.time.LocalDate
import java.util.function.Predicate


class RapportHeuresController(contexte: Contexte) : ControleurAbstrait(contexte) {
    @FXML
    private lateinit var beginDate: DatePicker

    @FXML
    private lateinit var beginValidation: Label

    @FXML
    private lateinit var endDate: DatePicker

    @FXML
    private lateinit var endValidation: Label

    @FXML
    private lateinit var employeeFilter: TextField

    @FXML
    private lateinit var employees: ListView<Employee>

    private lateinit var employeesList: FilteredList<Employee>

    private var selectedEmploye: Employee? = null

    fun initialize() {
        initializeDates()
        initializeEmployees()
    }

    private fun initializeDates() {
        val today = LocalDate.now()
        var diffVendredi = (today.dayOfWeek.value.toLong() + 2) % 7

        if (diffVendredi == 0L) {//Si vendredi prendre le vendredi passé
            diffVendredi = 7
        }

        val endOfPreviousWeek = today.minusDays(diffVendredi)
        val startOfPreviousWeek = endOfPreviousWeek.minusDays(4)

        beginDate.setValue(startOfPreviousWeek)
        endDate.setValue(endOfPreviousWeek)


        beginDate.valueProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                if (newValue.isBefore(endDate.value)) {
                    beginValidation.setText("")
                } else {
                    beginValidation.setText("La date de debut est après la date de fin.")
                }
            }
        }
        endDate.valueProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                if (beginDate.value.isBefore(newValue)) {
                    beginValidation.setText("")
                } else {
                    beginValidation.setText("La date de debut est après la date de fin.")
                }

                if (newValue.isBefore(LocalDate.now())) {
                    endValidation.setText("")
                } else {
                    endValidation.setText("La date de fin n'est pas encore passée.")
                }
            }
        }
    }

    private fun initializeEmployees() {
        employeesList = FilteredList<Employee>(
            FXCollections.observableList(
                EmployeDAO(
                    contexte.services.getService<ServiceBD>() as ServiceBD
                ).chargerTout().map { employe -> Employee(employe.nom + " " + employe.prenom) }
            )
        )

        val filteredEmployee = FilteredList(employeesList)

        filteredEmployee.predicateProperty().bind(
            Bindings.createObjectBinding(
                { createFilterPredicate(employeeFilter.text) },
                employeeFilter.textProperty()
            )
        )

        employees.setCellFactory(CheckBoxListCell.forListView
        { item -> item.selectedProperty() })
        employees.getSelectionModel().selectedItemProperty().addListener()
        { _, _, newValue ->
            selectedEmploye?.setSelected(false)
            newValue?.setSelected(true)
            selectedEmploye = newValue
        }

        employees.items = filteredEmployee
    }

    private fun createFilterPredicate(filterText: String): Predicate<Employee> {
        return Predicate { item ->
            filterText.isEmpty() || item.text.contains(filterText, ignoreCase = true)
        }
    }

    @FXML
    private fun generate() {

    }
}


class Employee(val text: String) {
    private val selected: BooleanProperty

    init {
        selected = SimpleBooleanProperty(false)
    }

    fun selectedProperty(): BooleanProperty {
        return selected
    }

    fun isSelected(): Boolean {
        return selected.get()
    }

    fun setSelected(selected: Boolean) {
        this.selected.set(selected)
    }

    override fun toString(): String {
        return text
    }
}