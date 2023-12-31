package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    allDrivers - this.trips.associateBy(Trip::driver).keys

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    this.allPassengers.filter { pass -> this.trips.count { trip -> pass in trip.passengers } >= minTrips }.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    this.allPassengers.filter { pass -> this.trips.count { trip -> pass in trip.passengers && driver == trip.driver } > 1 }
        .toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    this.allPassengers.filter { pass ->
        val passTrips = this.trips.filter { pass in it.passengers }
        val discCount = passTrips.count { (it.discount ?: 0.0) > 0.0 }
        val noDiscCount = passTrips.size - discCount
        discCount > noDiscCount
    }.toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    if (this.trips.isEmpty()) return null

    val durations = this.trips.map { it.duration / 10 * 10..it.duration / 10 * 10 + 9 }
    return durations.groupingBy { it }.eachCount().maxBy { it.value }.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (this.trips.isEmpty()) return false
    val driversIncome = this.trips.groupBy(Trip::driver)
        .map { (_, trips) ->
            trips.map { it.cost }.reduce { acc, cost -> acc + cost } //sum of costs per driver
        }.sortedDescending()

    val totalIncome = driversIncome.reduce { acc, income -> acc + income }
    val numDrivers = this.allDrivers.size

    var topIncome = 0.0
    for (i in 0 until (numDrivers * 0.2).toInt())
        topIncome += driversIncome[i]

    return topIncome >= totalIncome * 0.8
}