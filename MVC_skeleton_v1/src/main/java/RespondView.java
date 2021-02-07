public class RespondView implements ViewInterface{

    @Override
    public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {
        switch(operationName){
            case "insert" : return insert(modelData);
        }

        return null;
    }

    private ViewData insert(ModelData modelData) {
        //redirect if another process requests
        if(modelData.transferData.containsKey("redirectFunction")){
            modelData.transferData.put("insertRespondFlag",true);
            String function = (String) modelData.transferData.get("redirectFunction");
            String operation = (String) modelData.transferData.get("redirectOperation");
            return new ViewData(function,operation,modelData.transferData);
        }
        return new ViewData("Screen","screen.gui");
    }
}
