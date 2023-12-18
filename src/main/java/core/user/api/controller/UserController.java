package core.user.api.controller;

import core.user.api.common.UserFilter;
import core.user.api.common.YearEnd;
import core.user.api.dto.*;
import core.user.api.model.*;
import core.user.api.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@CrossOrigin
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final BusinessTypeService businessTypeService;
    private final PrivilegeMenuService privilegeMenuService;
    private final RolePropertyService rolePropertyService;
    private final AppRoleService appRoleService;
    private final MenuService menuService;
    private final PrivilegeCompanyService privilegeCompanyService;
    private final CompanyInfoService companyInfoService;
    private final CurrencyService currencyService;
    private final SystemPropertyService systemPropertyService;
    private final MachinePropertyService machinePropertyService;
    private final DepartmentUserService departmentUserService;
    private final ProjectService projectService;
    private final DateLockService dateLockService;
    private final ExchangeRateService exchangeRateService;
    private final AppUserService userService;
    private final MachineService machineService;
    private final CountryService countryService;

    @GetMapping("/hello")
    public Mono<?> hello() {
        return Mono.just("Hello.");
    }


    @GetMapping("/login")
    public Mono<ResponseEntity<AppUserDto>> login(@RequestParam String userName,
                                                  @RequestParam String password) {
        return userService.login(userName, password);
    }

    @GetMapping("/checkSerialNo")
    public Mono<MachineInfoDto> checkSerialNo(@RequestParam String serialNo) {
        return machineService.findBySerialNo(serialNo);
    }

    @GetMapping("/getMacList")
    public Flux<MachineInfoDto> getMacList() {
        return machineService.findAll();
    }

    @DeleteMapping("/deleteMac")
    public Mono<Boolean> deleteMac(@RequestParam Integer macId) {
        return machineService.deleteByMacId(macId);
    }

    @GetMapping("/updateAllMachine")
    public Mono<Boolean> updateAllMachine(@RequestParam boolean update) {
        return machineService.updateAllMachine(update);
    }


    @PostMapping("/saveMachine")
    public Mono<MachineInfoDto> saveMachine(@RequestBody MachineInfoDto machineInfo) {
        return machineService.saveOrUpdate(machineInfo);
    }

    @PostMapping("/saveUser")
    public Mono<AppUser> saveUser(@RequestBody AppUser user) {
        return userService.saveOrUpdate(user);
    }

    @PostMapping("/savePrivilegeCompany")
    public Mono<PrivilegeCompanyDto> savePrivilegeCompany(@RequestBody PrivilegeCompanyDto dto) {
        return privilegeCompanyService.saveOrUpdate(dto);
    }

    @GetMapping("/getPrivilegeCompany")
    public Flux<CompanyInfoDto> getPrivilegeCompany(@RequestParam String roleCode) {
        return privilegeCompanyService.getRoleCompany(roleCode);
    }

    @GetMapping("/getAppUser")
    public Flux<AppUserDto> getAppUser() {
        return userService.findAll();
    }

    @GetMapping("/findAppUser")
    public Mono<AppUser> findAppUser(@RequestParam String userCode) {
        return userService.findById(userCode);
    }

    @GetMapping("/getRoleMenuTree")
    public Flux<MenuDto> getRoleMenuTree(@RequestParam String roleCode, @RequestParam String compCode) {
        return menuService.getPrivilegeMenuTree(roleCode, compCode, false);
    }

    @GetMapping("/getPrivilegeRoleMenuTree")
    public Flux<MenuDto> getPrivilegeRoleMenuTree(@RequestParam String roleCode, @RequestParam String compCode) {
        return menuService.getPrivilegeMenuTree(roleCode, compCode, true);
    }

    @GetMapping("/getMenuTree")
    public Flux<MenuDto> getMenuFlux(@RequestParam String compCode) {
        return menuService.getMenuTree("#", compCode);
    }

    @GetMapping("/getMenuParent")
    public Flux<MenuDto> getMenuParent(@RequestParam String compCode) {
        return menuService.getMenuDynamic(compCode);
    }

    @PostMapping(path = "/saveMenu")
    public Mono<MenuDto> saveMenu(@RequestBody MenuDto dto) {
        return menuService.saveOrUpdate(dto);
    }

    @PostMapping(path = "/deleteMenu")
    public Mono<Boolean> deleteMenu(@RequestBody MenuDto dto) {
        return menuService.deleteMenu(dto);
    }

    @GetMapping("/getReport")
    public Flux<MenuDto> getReport(@RequestParam String roleCode,
                                   @RequestParam String menuClass,
                                   @RequestParam String compCode) {
        return menuService.getReport(roleCode, menuClass, compCode);
    }

    @GetMapping("/getRole")
    public Flux<AppRole> getRole() {
        return appRoleService.findAll();
    }

    @PostMapping(path = "/saveRole")
    public Mono<AppRole> saveRole(@RequestBody AppRole role) {
        return appRoleService.saveOrUpdate(role);
    }

    @GetMapping(path = "/findCompany")
    public Mono<CompanyInfo> findCompany(@RequestParam String compCode) {
        return companyInfoService.findById(compCode);
    }

    @GetMapping(path = "/findRole")
    public Mono<AppRole> findRole(@RequestParam String roleCode) {
        return appRoleService.findById(roleCode);
    }

    @PostMapping(path = "/savePrivilegeMenu")
    public Mono<PrivilegeMenuDto> savePrivilegeMenu(@RequestBody PrivilegeMenuDto dto) {
        return privilegeMenuService.saveOrUpdate(dto);
    }

    @GetMapping(path = "/getRoleProperty")
    public Flux<RolePropertyDto> getRoleProperty(@RequestParam String roleCode, @RequestParam String compCode) {
        return rolePropertyService.getRoleProperty(roleCode, compCode);
    }

    @GetMapping(path = "/getPrivilegeRoleCompany")
    public Flux<CompanyInfoDto> getRoleCompany(@RequestParam String roleCode) {
        return privilegeCompanyService.getRoleCompany(roleCode);
    }


    @PostMapping(path = "/saveRoleProperty")
    public Mono<RolePropertyDto> saveRoleProperty(@RequestBody RolePropertyDto dto) {
        return rolePropertyService.saveOrUpdate(dto);
    }

    @PostMapping(path = "/deleteRoleProperty")
    public Mono<Boolean> deleteRoleProperty(@RequestBody RoleProperty rp) {
        return rolePropertyService.delete(rp);
    }

    @PostMapping("/saveCurrency")
    public Mono<Currency> saveCurrency(@RequestBody Currency currency) {
        return currencyService.saveOrUpdate(currency);
    }

    @GetMapping("/getCurrency")
    public Flux<Currency> getCurrency() {
        return currencyService.findAll();
    }

    @GetMapping("/findCurrency")
    public Mono<Currency> findCurrency(@RequestParam String curCode) {
        return currencyService.findById(curCode);
    }

    @PostMapping("/saveCompany")
    public Mono<CompanyInfo> saveCompany(@RequestBody CompanyInfo info) {
        return companyInfoService.saveOrUpdate(info);
    }

    @PostMapping("/saveSystemProperty")
    public Mono<SystemPropertyDto> saveSystemProperty(@RequestBody SystemPropertyDto property) {
        return systemPropertyService.saveOrUpdate(property);
    }

    @PostMapping("/saveMacProperty")
    public Mono<MachinePropertyDto> saveMacProperty(@RequestBody MachinePropertyDto dto) {
        return machinePropertyService.saveOrUpdate(dto);
    }

    @GetMapping("/getCompany")
    public Flux<CompanyInfo> getCompany(@RequestParam Boolean active) {
        return companyInfoService.findAll();
    }

    @GetMapping("/getSystemProperty")
    public Flux<SystemPropertyDto> getSystemProperty(@RequestParam String compCode) {
        return systemPropertyService.getSystemProperty(compCode);
    }

    @GetMapping("/getMacProperty")
    public Flux<MachinePropertyDto> getMacProperty(@RequestParam Integer macId) {
        return machinePropertyService.getMacProperty(macId);
    }

    @GetMapping(path = "/getProperty")
    public Mono<HashMap<String, String>> getProperty(@RequestParam String compCode, @RequestParam String roleCode, @RequestParam Integer macId) {
        return systemPropertyService.getProperty(compCode, roleCode, macId).next();
    }


    @GetMapping(path = "/getDepartment")
    public Flux<DepartmentUserDto> getDepartment(@RequestParam Boolean active,
                                              @RequestParam String compCode) {
        return departmentUserService.getDepartment(active, compCode);

    }

    @PostMapping("/findDepartment")
    public Mono<DepartmentUserDto> findDepartment(@RequestParam Integer deptId, @RequestParam String compCode) {
        return departmentUserService.findById(deptId, compCode);
    }

    @PostMapping("/saveDepartment")
    public Mono<DepartmentUserDto> saveDepartment(@RequestBody DepartmentUserDto dto) {
        return departmentUserService.saveOrUpdate(dto);
    }

    @GetMapping(path = "/getBusinessType")
    public Flux<?> getBusinessType() {
        return businessTypeService.findAll();
    }

    @GetMapping(path = "/findBusinessType")
    public Mono<BusinessType> findBusinessType(@RequestParam Integer id) {
        return businessTypeService.findById(id);
    }

    @PostMapping(path = "/saveBusinessType")
    public Mono<BusinessType> saveBusinessType(@RequestBody BusinessType type) {
        return businessTypeService.saveOrUpdate(type);
    }

    @PostMapping(path = "/saveProject")
    public Mono<ProjectDto> saveProject(@RequestBody ProjectDto dto) {
        return projectService.saveOrUpdate(dto);
    }

    @GetMapping(path = "/findProject")
    public Mono<ProjectDto> findProject(@RequestParam String projectNo, @RequestParam String compCode) {
        return projectService.findById(projectNo, compCode);
    }

    @GetMapping(path = "/searchProject")
    public Flux<ProjectDto> searchProject(@RequestParam String compCode) {
        return projectService.findAll(compCode);
    }

    @GetMapping(path = "/searchProjectByCode", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProjectDto> searchProjectByCode(@RequestParam String code, @RequestParam String compCode) {
        return projectService.autoCompete(code, compCode);
    }

    @PostMapping(path = "/saveExchange")
    public Mono<ExchangeRateDto> saveExchange(@RequestBody ExchangeRateDto dto) {
        return exchangeRateService.saveOrUpdate(dto);
    }

    @PostMapping(path = "/deleteExchange")
    public Mono<Boolean> deleteExchange(@RequestParam String exCode, @RequestParam String compCode) {
        return exchangeRateService.delete(exCode, compCode);
    }

    @GetMapping(path = "/searchExchange")
    public Flux<ExchangeRateDto> searchExchange(@RequestParam String startDate,
                                                @RequestParam String endDate,
                                                @RequestParam String targetCur,
                                                @RequestParam String compCode) {
        return exchangeRateService.search(startDate, endDate, targetCur, compCode);
    }

    @PostMapping(path = "/getExchangeAvg")
    public Mono<ExchangeRateDto> getExchangeAvg(@RequestBody UserFilter filter) {
        return exchangeRateService.getAvgRate(filter);
    }

    @PostMapping(path = "/getExchangeRecent")
    public Mono<ExchangeRateDto> getExchangeRecent(@RequestBody UserFilter filter) {
        return exchangeRateService.getRecentRate(filter);
    }

    @PostMapping(path = "/yearEnd")
    public Mono<CompanyInfo> yearEnd(@RequestBody YearEnd end) {
        return companyInfoService.yearEnd(end);
    }

    @GetMapping("/getUserByDate")
    public Flux<?> getUserByDate(@RequestParam String updatedDate) {
        return userService.getAppUserByDate(updatedDate);
    }

    @GetMapping("/getBusinessTypeByDate")
    public Flux<BusinessType> getBusinessTypeByDate(@RequestParam String updatedDate) {
        return businessTypeService.getBusinessTypeByDate(updatedDate);
    }

    @GetMapping("/getCompanyInfoByDate")
    public Flux<CompanyInfo> getCompanyInfoByDate(@RequestParam String updatedDate) {
        return companyInfoService.getCompanyByDate(updatedDate);
    }

    @GetMapping("/getCurrencyByDate")
    public Flux<Currency> getCurrencyByDate(@RequestParam String updatedDate) {
        return currencyService.getCurrencyByDate(updatedDate);
    }

    @GetMapping("/getDepartmentByDate")
    public Flux<DepartmentUserDto> getDepartmentByDate(@RequestParam String updatedDate) {
        return departmentUserService.getDepartmentByDate(updatedDate);
    }

    @GetMapping("/getExchangeRateByDate")
    public Flux<ExchangeRateDto> getExchangeRateByDate(@RequestParam String updatedDate) {
        return exchangeRateService.getExchangeByDate(updatedDate);
    }

    @GetMapping("/getMacPropertyByDate")
    public Flux<MachinePropertyDto> getMacPropertyByDate(@RequestParam String updatedDate) {
        return machinePropertyService.getMachinePropertyByDate(updatedDate);
    }

    @GetMapping("/getMachineInfoByDate")
    public Flux<MachineInfoDto> getMachineInfoByDate(@RequestParam String updatedDate) {
        return machineService.getMachineByDate(updatedDate);
    }

    @GetMapping("/getMenuByDate")
    public Flux<MenuDto> getMenuByDate(@RequestParam String updatedDate) {
        return menuService.getMenuByDate(updatedDate);
    }

    @GetMapping("/getPCByDate")
    public Flux<PrivilegeCompanyDto> getPCByDate(@RequestParam String updatedDate) {
        return privilegeCompanyService.getProjectByDate(updatedDate);
    }

    @GetMapping("/getPMByDate")
    public Flux<?> getPMByDate(@RequestParam String updatedDate) {
        return privilegeMenuService.getProjectByDate(updatedDate);
    }

    @GetMapping("/getProjectByDate")
    public Flux<ProjectDto> getProjectByDate(@RequestParam String updatedDate) {
        return projectService.getProjectByDate(updatedDate);
    }

    @GetMapping("/getRoleByDate")
    public Flux<AppRole> getRoleByDate(@RequestParam String updatedDate) {
        return appRoleService.getRoleByDate(updatedDate);
    }

    @GetMapping("/getRolePropByDate")
    public Flux<?> getRolePropByDate(@RequestParam String updatedDate) {
        return rolePropertyService.getSystemPropertyByDate(updatedDate);
    }


    @GetMapping("/getSystemPropertyByDate")
    public Flux<SystemPropertyDto> getSystemPropertyByDate(@RequestParam String updatedDate) {
        return systemPropertyService.getSystemPropertyByDate(updatedDate);
    }

    @GetMapping("/getDateLockByDate")
    public Flux<DateLockDto> getDateLockByDate(@RequestParam String updatedDate) {
        return dateLockService.getDateLockByDate(updatedDate);
    }

    @GetMapping("/getExchangeRate")
    public Flux<ExchangeRateDto> getExchangeRate(@RequestParam String compCode) {
        return exchangeRateService.findAll(compCode);
    }

    @GetMapping("/findExchange")
    public Mono<ExchangeRateDto> findExchange(@RequestParam String exCode, @RequestBody String compCode) {
        return exchangeRateService.findById(exCode, compCode);
    }

    @GetMapping("/getCountry")
    public Flux<Country> getCountry() {
        return countryService.findAll();
    }

    @GetMapping("/findCountry")
    public Mono<Country> findCountry(@RequestParam String id) {
        return countryService.findById(id);
    }

    @GetMapping("/getDateLock")
    public Flux<DateLockDto> getDateLock(@RequestParam String compCode) {
        return dateLockService.findAll(compCode);
    }

    @PostMapping("/saveDateLock")
    public Mono<DateLockDto> saveDateLock(@RequestBody DateLock dl) {
        return dateLockService.saveOrUpdate(dl);
    }
}
