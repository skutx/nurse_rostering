package model.schedule;

import helper.DateTimeHelper;
import helper.RandomHelper;

import java.util.*;

/**
 * Scheduling period model.
 */
public class SchedulingPeriod {
    /**
     * Identifier.
     */
    private String id;

    /**
     * Path to source file.
     */
    private String sourceFile;

    /**
     * StartEvolutionaryAlgorithm date of schedule period.
     */
    private Date startDate;

    /**
     * End date of schedule period.
     */
    private Date endDate;

    /**
     * List of available skills.
     */
    private List<Skill> skills = new ArrayList<Skill>();

    /**
     * List of required shift types.
     */
    private List<ShiftType> shiftTypes = new ArrayList<ShiftType>();

    /**
     * List of available contracts.
     */
    private List<Contract> contracts = new ArrayList<Contract>();

    /**
     * List of available employees.
     */
    private List<Employee> employees = new ArrayList<Employee>();

    /**
     * List of required coverings for each days.
     */
    private List<Cover> dayCovers = new ArrayList<Cover>();

    /**
     * Map of identifier -> pattern.
     */
    private Map<Integer, Pattern> patterns = new LinkedHashMap<Integer, Pattern>();

    /**
     * Sets the source file path.
     * @param path Source file path.
     */
    public void setSourceFile(String path) {
        sourceFile = path;
    }

    /**
     * Returns a contract by its identifier.
     * @param id Contract identifier.
     * @return Contract instance or null.
     */
    public Contract getContractById(int id) {
        for (Contract contract: contracts) {
            if (contract.getId() == id) {
                return contract;
            }
        }

        return null;
    }

    /**
     * Returns a ShiftType instance by identifier.
     * @param id ShiftType identifier.
     * @return ShiftType instance or null.
     */
    public ShiftType getShiftTypeById(String id) {
        for (ShiftType shift: shiftTypes) {
            if (shift.getId().equals(id)) {
                return shift;
            }
        }

        return null;
    }

    /**
     * Returns an Employee instance by identifier.
     * @param id Employee identifier.
     * @return Employee instance or null.
     */
    public Employee getEmployeeById(String id) {
        return getEmployeeById(Integer.parseInt(id));
    }

    /**
     * Returns an Employee instance by identifier as integer.
     * @param id Employee identifier as integer.
     * @return Employee instance or null.
     */
    public Employee getEmployeeById(int id) {
        for (Employee employee: employees) {
            if (employee.getId() == id) {
                return employee;
            }
        }

        return null;
    }

    /**
     * Adds an employee.
     * @param employee Employee instance.
     */
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    /**
     * Adds a shift type.
     * @param shiftType ShiftType instance.
     */
    public void addShiftType(ShiftType shiftType) {
        shiftTypes.add(shiftType);
    }

    /**
     * Returns cover information by day.
     * @param day Day instance
     * @return Map instance or null.
     */
    private Map<ShiftType, Integer> getCoversByDay(Day day) {
        for (Cover cover: dayCovers) {
            if (cover.getDay().equals(day)) {
                return cover.getCovers();
            }
        }

        return null;
    }

    /**
     * Returns cover information by day.
     * @param date Date instance
     * @return Map instance or null.
     */
    public Map<ShiftType, Integer> getCoversByDate(Date date) {
        return getCoversByDay(DateTimeHelper.getInstance().getDayByDate(date));
    }

    /***
     * Following getters and setters are trivial and self explanatory, therefore not documented further.
     ***/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public Employee getRandomEmployee() {
        return employees.get(RandomHelper.getInstance().getInt(employees.size()));
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    /**
     * Returns the day covers list.
     * @return List of Cover instances
     */
    public List<Cover> getDayCovers() {
        return dayCovers;
    }

    public void setDayCovers(List<Cover> dayCovers) {
        this.dayCovers = dayCovers;
    }

    /**
     * Adds a Pattern instance.
     * @param pattern Pattern instance
     */
    public void addPattern(Pattern pattern) {
        patterns.put(pattern.getId(), pattern);
    }

    /**
     * Returns a Pattern by given patternId if available, otherwise null
     * @param patternId Pattern identifier
     * @return Pattern instance by given patternId if available, otherwise null
     */
    public Pattern getPatternById(int patternId) {
        for (Map.Entry<Integer, Pattern> entry: patterns.entrySet()) {
            if (entry.getKey() == patternId) {
                return entry.getValue();
            }
        }

        return null;
    }

    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder out = new StringBuilder();
        out.append("Scheduling period information").append(nl)
                .append(" ID: ").append(id).append(nl)
                .append(" StartEvolutionaryAlgorithm date: ").append(startDate).append(nl)
                .append(" End date: ").append(endDate).append(nl)
                .append(" Source file: ").append(sourceFile).append(nl);

        // add required skills, if any
        if (skills.size() > 0) {
            out.append(" Required skills: ").append(skills.toString()).append(nl);
        }

        // add shift types, if any
        if (shiftTypes.size() > 0) {
            out.append(" Shift types: ").append(nl);
            for (ShiftType shiftType: shiftTypes) {
                out.append(" - ").append(shiftType.toString()).append(nl);
            }
        }

        // add contracts, if any
        if (contracts.size() > 0) {
            out.append(" Contracts: ").append(nl);
            for (Contract contract: contracts) {
                out.append(" - ").append(contract.toString()).append(nl);
            }
        }

        // add employees, if any
        if (employees.size() > 0) {
            out.append(" Employees: ").append(nl);
            for (Employee employee: employees) {
                out.append(" - ").append(employee.toString()).append(nl);
            }
        }

        // add day covers, if any
        if (dayCovers.size() > 0) {
            out.append(" Day covers: ").append(nl);
            for (Cover dayCover: dayCovers) {
                out.append(" - ").append(dayCover.toString()).append(nl);
            }
        }

        return out.toString();
    }

    /**
     * Returns the preferred number of employees requested for a shift type.
     * @param shiftType ShiftType instance
     * @param date Date instance
     * @return Count of preferred employees per shift type
     */
    public Integer getPreferredEmployeeCount(ShiftType shiftType, Date date) {
        for (Cover cover: dayCovers) {
            Integer count = cover.getPreferredEmployeeCount(shiftType, date);
            if (count != null) {
                return count;
            }
        }

        return null;
    }
}
