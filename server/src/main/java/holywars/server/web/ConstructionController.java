package holywars.server.web;

import holywars.server.game.ConstructionService;
import holywars.town.BuildingType;
import holywars.town.InvalidBuildingSlotPositionException;
import holywars.town.MismatchedBuildingTypeException;
import holywars.town.NotEnoughResourcesException;
import holywars.town.SlotNotFreeException;
import holywars.town.TownId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
class ConstructionController {

    private final ConstructionService constructionService;

    ConstructionController(ConstructionService constructionService) {
        this.constructionService = constructionService;
    }

    @PostMapping("/towns/{id}/slots/{position}/build")
    String build(
            @PathVariable("id") int id,
            @PathVariable("position") int position,
            @RequestParam("type") BuildingType type,
            RedirectAttributes redirectAttributes) {
        try {
            constructionService.startConstruction(new TownId(id), position, type);
        } catch (NotEnoughResourcesException exception) {
            redirectAttributes.addFlashAttribute("error", "No alcanzan los recursos");
        } catch (SlotNotFreeException exception) {
            redirectAttributes.addFlashAttribute("error", "La parcela no está libre");
        } catch (MismatchedBuildingTypeException exception) {
            redirectAttributes.addFlashAttribute("error", "Ese edificio no va en esa parcela");
        } catch (InvalidBuildingSlotPositionException exception) {
            redirectAttributes.addFlashAttribute("error", "La parcela no existe");
        }
        return "redirect:/towns/" + id;
    }
}
